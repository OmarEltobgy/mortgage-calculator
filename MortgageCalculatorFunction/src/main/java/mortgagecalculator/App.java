package mortgagecalculator;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String ERROR_RESPONSE_TEMPLATE = "{\"Error\": \"%s\"}";
    private static final String INVALID_INPUT_ERROR = "Input is invalid";
    private static final String INPUT_PARSING_ERROR = "Input parsing error";
    private static final String OUTPUT_BODY = "{\"mortgagePayment\": \"%s\"}";

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent inputEvent, final Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Input body to mortgage calculator: " + inputEvent.getBody());
        String outputBody;
        int outputResponseCode;
        try {
            MortgageCalculatorInput mortgageCalculatorInputObject = gson.fromJson(inputEvent.getBody(), MortgageCalculatorInput.class);
            logger.log("Input object to mortgage calculator: " + mortgageCalculatorInputObject.toString());
            boolean validInput = MortgageCalculatorInput.validate(mortgageCalculatorInputObject);
            if (!validInput) {
                throw new InvalidInputException("Invalid Input");
            }
            double payment = MortgageCalculatorUtils.calculateMortgagePayment(mortgageCalculatorInputObject);
            outputBody = String.format(OUTPUT_BODY, payment);;
            outputResponseCode = 200;
            getResponse(outputBody, outputResponseCode);
        } catch (JsonSyntaxException e) {
            logger.log("Error while parsing input body, exception: " + e);
            outputResponseCode = 400;
            outputBody = String.format(ERROR_RESPONSE_TEMPLATE, INPUT_PARSING_ERROR);
        } catch (InvalidInputException e) {
            logger.log("Input is invalid");
            outputResponseCode = 400;
            outputBody = String.format(ERROR_RESPONSE_TEMPLATE, INVALID_INPUT_ERROR);
        }
        return getResponse(outputBody, outputResponseCode);
    }

    private static APIGatewayProxyResponseEvent getResponse(String outputBody, int outputResponseCode) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(outputResponseCode)
                .withBody(outputBody);
    }
}
