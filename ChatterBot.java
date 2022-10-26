import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 * @author Dan Nirel
 */
class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";


    Random rand = new Random();
    String[] repliesToIllegalRequest;
    String[] repliesToLegalRequest;
    String name;

    /**
     * constructor
     * @param in_name given name of the chatter bot
     * @param repliesToLegalRequest list of responses to legal requests
     * @param repliesToIllegalRequest list of responses to illegal requests
     */
    ChatterBot(String in_name,String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.name = in_name;
        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        for(int i = 0 ; i < repliesToLegalRequest.length ; i = i+1) {
            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
        }
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        for(int i = 0 ; i < repliesToIllegalRequest.length ; i = i+1) {
            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
        }
    }

    /**
     * replaces the placehoder with a given phrase
     * @param options list of options for replacements
     * @param placeHolder the place holder that we wish to replace
     * @param statement the phrase that we wish to insert instead of the placeholder
     * @return updated statement
     */
    String replacePlaceholderInARandomPattern(String[] options, String placeHolder,String statement ){
        int randomIndex = rand.nextInt(options.length);
        String responsePattern = options[randomIndex];
        return responsePattern.replaceAll(placeHolder,statement);
    }

    /**
     * reply method for chatter bot
     * @param statement given statement to reply to
     * @return updated statement of the bot as a response
     */
    String replyTo(String statement) {
        if(statement.startsWith(REQUEST_PREFIX)) {
            //we don’t repeat the request prefix, so delete it from the reply
            statement = statement.replaceFirst(REQUEST_PREFIX, "");
            return replacePlaceholderInARandomPattern(repliesToLegalRequest,REQUESTED_PHRASE_PLACEHOLDER,statement);
        }
        return replacePlaceholderInARandomPattern(repliesToIllegalRequest,ILLEGAL_REQUEST_PLACEHOLDER,statement);
    }

    /**
     * responds to an illegal request
     * @param statement the given statement to reply to
     * @return updated statement
     */
    String respondToIllegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
        String reply = repliesToIllegalRequest[randomIndex];
        reply = reply + statement;
        return reply;
    }

    /**
     *
     * @param statement the given statement to reply to
     * @return updated statement
     */
    String respondToLegalRequest(String statement) {
        String phrase = statement.replaceFirst(REQUEST_PREFIX,"");
        int randomIndex = rand.nextInt(repliesToLegalRequest.length);
        String responsePattern = repliesToLegalRequest[randomIndex];
        return responsePattern.replaceAll(REQUESTED_PHRASE_PLACEHOLDER, phrase);
    }
    /*
    returns the name of the current bot
     */
    String getName(){
        return this.name;
    }
}
