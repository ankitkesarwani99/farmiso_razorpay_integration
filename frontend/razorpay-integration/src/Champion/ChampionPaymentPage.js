import "./ChampionPaymentPage.css";
import GenericErrorPage from "../Error/GenericErrorPage";
import { useEffect, useState } from "react";

const backendEndpoint = "http://localhost:8098/api/v1/champion/";

function loadRazorpay(scriptName) {
  return new Promise((resolve) => {
    const script = document.createElement("script");
    script.src = scriptName;

    script.onload = () => {
      resolve(true);
    };
    script.onerror = () => {
      resolve(false);
    };
    document.body.appendChild(script);
  });
}

function ChampionPaymentPage(props) {
  const [amount, setAmount] = useState(0);
  const [name, setName] = useState("");
  const [error, setError] = useState({
    hasError: false,
    errorMessage: "",
  });

  function handleError(response, data) {
    console.log("Backend Response==>", response, data);
    if (response.status === 400 || response.status === 500) {
      setError({
        hasError: true,
        errorMessage: data.errors,
      });
    }
  }

  async function getPaymentDetailForChampion(id) {
    const requestOptions = {
      crossDomain: true,
      method: "GET",
    };
    const url = backendEndpoint + id;
    const response = await fetch(url, requestOptions);
    const data = await response.json();
    handleError(response, data);
    return data;
  }
  async function savePaymentData(paymentData, paymentReferenceId) {
    const requestOptions = {
      crossDomain: true,
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        payment_reference_id: paymentReferenceId,
        razorpay_payment_response: paymentData,
      }),
    };

    const url = backendEndpoint + "order/confirm";
    const response = await fetch(url, requestOptions);
    const data = await response.json();
    handleError(response, data);
    return data;
  }

  async function getOrderDetails(amountPending, championId) {
    const requestOptions = {
      crossDomain: true,
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        champion_id: championId,
        amount: amountPending,
        currency: "INR",
      }),
    };
    const url = backendEndpoint + "order/create";
    const response = await fetch(url, requestOptions);
    const data = await response.json();
    handleError(response, data);
    return data;
  }

  useEffect(() => {
    const data = getPaymentDetailForChampion(props.id);
    data
      .then((details) => {
        if (details.data !== undefined) {
          setAmount(details.data.amount);
          setName(details.data.name);
        }
      })
      .catch((err) => {
        setError({
          hasError: true,
          errorMessage: err,
        });
      });
  }, []);

  async function makePayment() {
    try {
      const res = await loadRazorpay(
        "https://checkout.razorpay.com/v1/checkout.js"
      );

      if (!res) {
        console.log("Razorpay not loaded");
        return;
      }

      getOrderDetails(amount, props.id).then((details) => {
        if (error.hasError) {
          return;
        }
        var options = {
          key: "rzp_test_GiIH2iYTJeNbKp",
          amount: amount * 100,
          currency: "INR",
          name: "Farmiso",
          description: "Test Transaction",
          image:
            "https://upload.wikimedia.org/wikipedia/commons/3/33/Vanamo_Logo.png",
          order_id: details.data.order_id,
          handler: function (response) {
            const backendResponse = savePaymentData(
              response,
              details.data.payment_reference_id
            );
            backendResponse.then((val) => {
              if (error.hasError) {
                return;
              }
              if (val.data.success === true) {
                alert("Payment Successfull!!!");
                setAmount(0);
              } else {
                alert("Payment Failed!!!");
              }
            });
          },
          theme: {
            color: "#3399cc",
          },
        };

        var rzp = new window.Razorpay(options);
        rzp.on("payment.failed", function (response) {
          console.log("payment failed ==>", response.error);
          alert(response.error.reason);
        });

        rzp.open();
      });
    } catch (error) {
      setError({
        hasError: true,
        errorMessage: error,
      });
    }
  }
  return (
    <>
      {error.hasError === true ? (
        <GenericErrorPage errorMessage={error.errorMessage}></GenericErrorPage>
      ) : amount > 0 ? (
        <div className="payment__controls">
          <div className="payment__control">
            <span>Champion Name :</span>
            <span>{name}</span>
          </div>
          <div className="new-payment__control">
            <span>Pending Amount in Rs. </span>
            <span>{amount}</span>
          </div>
          <div className="payment__actions">
            <button id="payment_button" onClick={makePayment}>
              Pay
            </button>
          </div>
        </div>
      ) : (
        <h1 style={{ color: "blue", textAlign: "center" }}>
          No pending amount!!!
        </h1>
      )}
    </>
  );
}

export default ChampionPaymentPage;
