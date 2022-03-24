import { useParams } from "react-router-dom";
import ChampionPaymentPage from "./ChampionPaymentPage";
function ChampionPage() {
  const { id } = useParams();
  console.log("champion id==>", id);
  return <ChampionPaymentPage id={id}></ChampionPaymentPage>;
}
export default ChampionPage;
