import "./GenericErrorPage.css";
const GenericErrorPage = (props) => {
  return (
    <div>
      <h2 className="error_page_content">Oops! Something went wrong</h2>
      <h3 className="error_page_content">{`Message : ${props.errorMessage}`}</h3>
    </div>
  );
};
export default GenericErrorPage;
