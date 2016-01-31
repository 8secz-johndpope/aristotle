package aws.services.cloudsearchv2;

import java.util.HashMap;
import java.util.Map;

import aws.services.cloudsearchv2.documents.AmazonCloudSearchAddRequest;
import aws.services.cloudsearchv2.documents.AmazonCloudSearchDeleteRequest;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudsearchv2.model.DateOptions;
import com.amazonaws.services.cloudsearchv2.model.DefineIndexFieldRequest;
import com.amazonaws.services.cloudsearchv2.model.DescribeDomainsRequest;
import com.amazonaws.services.cloudsearchv2.model.DescribeDomainsResult;
import com.amazonaws.services.cloudsearchv2.model.DescribeIndexFieldsRequest;
import com.amazonaws.services.cloudsearchv2.model.DescribeIndexFieldsResult;
import com.amazonaws.services.cloudsearchv2.model.DomainStatus;
import com.amazonaws.services.cloudsearchv2.model.IndexField;
import com.amazonaws.services.cloudsearchv2.model.IndexFieldStatus;
import com.amazonaws.services.cloudsearchv2.model.IndexFieldType;
import com.amazonaws.services.cloudsearchv2.model.TextArrayOptions;
import com.amazonaws.services.cloudsearchv2.model.TextOptions;
import com.amazonaws.util.json.JSONException;


public class AwsCloudSearchExample {
    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";
    public static final String FATHER_NAME_FIELD = "fname";
    public static final String MOTHER_NAME_FIELD = "mname";
    public static final String ADDRESS_FIELD = "address";
    public static final String CREATION_TYPE_FIELD = "creationtype";
    public static final String DATE_OF_BIRTH_FIELD = "dob";
    public static final String NRI_FIELD = "nri";
    public static final String NRI_COUNTRY_FIELD = "nricountry";
    public static final String NRI_COUNTRY_ID_FIELD = "nricountryid";
    public static final String NRI_COUNTRY_REGION_FIELD = "nricountryregion";
    public static final String NRI_COUNTRY_REGION_ID_FIELD = "nricountryregionid";
    public static final String NRI_COUNTRY_REGION_AREA_FIELD = "nricountryregionarea";
    public static final String NRI_COUNTRY_REGION_AREA_ID_FIELD = "nricountryregionareaid";
    public static final String LIVING_STATE_FIELD = "livingstate";
    public static final String LIVING_STATE_ID_FIELD = "livingstateid";
    public static final String LIVING_DISTRICT_FIELD = "livingdistrict";
    public static final String LIVING_DISTRICT_ID_FIELD = "livingdistrictid";
    public static final String LIVING_AC_FIELD = "livingac";
    public static final String LIVING_AC_ID_FIELD = "livingacid";
    public static final String LIVING_PC_FIELD = "livingpc";
    public static final String LIVING_PC_ID_FIELD = "livingpcid";

    public static final String VOTING_STATE_FIELD = "votingstate";
    public static final String VOTING_STATE_ID_FIELD = "votingstateid";
    public static final String VOTING_DISTRICT_FIELD = "votingdistrict";
    public static final String VOTING_DISTRICT_ID_FIELD = "votingdistrictid";
    public static final String VOTING_AC_FIELD = "votingac";
    public static final String VOTING_AC_ID_FIELD = "votingacid";
    public static final String VOTING_PC_FIELD = "votingpc";
    public static final String VOTING_PC_ID_FIELD = "votingpcid";
    public static final String PROFILE_FIELD = "profile";
    public static final String FACEBOOK_ACCOUNT_FIELD = "facebookaccounts";
    public static final String TWITTER_ACCOUNT_FIELD = "twitteraccounts";
    public static final String DONATION_FIELD = "donations";
    public static final String EMAIL_FIELD = "emails";
    public static final String PHONE_FIELD = "phones";
    public static final String INTEREST_FIELD = "interests";
    public static final String PROFILE_PIC_FIELD = "profilepic";
    public static final String MEMBER_FIELD = "member";
    public static final String DONOR_FIELD = "donor";
    public static final String VOLUNTEER_FIELD = "volunteer";
    public static final String VOTER_ID_FIELD = "voterid";

    private static AmazonCloudSearchClient amazonCloudSearchClient;
    private static Map<String, String> indexedField;
    public static void main(String[] args) throws AmazonCloudSearchRequestException, AmazonCloudSearchInternalServerException, JSONException {
        String searchEndpoint = "search-sa-users-vog5arqjslh4wkqhjcnncwia44.us-west-2.cloudsearch.amazonaws.com";
        String documentEndpoint = "doc-sa-users-vog5arqjslh4wkqhjcnncwia44.us-west-2.cloudsearch.amazonaws.com";
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAIXBOMVIQJQ47RDCQ", "KUCJTkdFF0kmqOtFokOyGv8fmBJv23Fz4hQXeLRf");

        AmazonCloudSearchClient client = new AmazonCloudSearchClient(awsCredentials);
        client.setSearchEndpoint(searchEndpoint);
        client.setDocumentEndpoint(documentEndpoint);
        client.setEndpoint("cloudsearch.us-west-2.amazonaws.com");
        client.setServiceNameIntern("cloudsearch");

        amazonCloudSearchClient = client;


        AmazonCloudSearchAddRequest addRequest = new AmazonCloudSearchAddRequest();
        addRequest.id = "1234";
        addRequest.version = 1;
        addRequest.addField("name", "Kiyansh Sharma");
        addRequest.addField("membeship", "Active");
        addRequest.addField("test", "Some Data");

        // client.addDocument(addRequest);
        DescribeDomainsRequest describeDomainsRequest = new DescribeDomainsRequest();

        DescribeDomainsResult describeDomainsResult = client.describeDomains(describeDomainsRequest);
        for (DomainStatus oneDomainStatus : describeDomainsResult.getDomainStatusList()) {
            System.out.println(oneDomainStatus.getDomainName());

        }
        DescribeIndexFieldsRequest describeIndexFieldsRequest = new DescribeIndexFieldsRequest();
        describeIndexFieldsRequest.setDomainName("sa-users");

        DescribeIndexFieldsResult describeIndexFieldsResult = amazonCloudSearchClient.describeIndexFields(describeIndexFieldsRequest);

        indexedField = new HashMap<String, String>();
        for (IndexFieldStatus indexFieldStatus : describeIndexFieldsResult.getIndexFields()) {
            System.out.println(indexFieldStatus.getOptions().getIndexFieldName() +" : "+ indexFieldStatus.getOptions().getIndexFieldType());
            indexedField.put(indexFieldStatus.getOptions().getIndexFieldName() , indexFieldStatus.getOptions().getIndexFieldType());
        }
        /*
        ensureTextIndex(NAME_FIELD, true, true, false);
        ensureTextIndex(ID_FIELD, true, false, false);
        ensureTextIndex(FATHER_NAME_FIELD, true, false, false);
        ensureTextIndex(MOTHER_NAME_FIELD, true, false, false);
        ensureTextIndex(ADDRESS_FIELD, true, false, false);
        ensureTextIndex(CREATION_TYPE_FIELD, true, false, false);
        ensureDateIndex(DATE_OF_BIRTH_FIELD, true, true, true);
        ensureTextIndex(NRI_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_ID_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_ID_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_AREA_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_AREA_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_STATE_FIELD, true, false, false);
        ensureTextIndex(LIVING_STATE_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_DISTRICT_FIELD, true, false, false);
        ensureTextIndex(LIVING_DISTRICT_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_AC_FIELD, true, false, false);
        ensureTextIndex(LIVING_AC_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_PC_FIELD, true, false, false);
        ensureTextIndex(LIVING_PC_ID_FIELD, true, false, false);

        ensureTextIndex(VOTING_STATE_FIELD, true, false, false);
        ensureTextIndex(VOTING_STATE_ID_FIELD, true, false, false);
        ensureTextIndex(VOTING_DISTRICT_FIELD, true, false, false);
        ensureTextIndex(VOTING_DISTRICT_ID_FIELD, true, false, false);
        ensureTextIndex(VOTING_AC_FIELD, true, false, false);
        ensureTextIndex(VOTING_AC_ID_FIELD, true, false, false);
        ensureTextIndex(VOTING_PC_FIELD, true, false, false);
        ensureTextIndex(VOTING_PC_ID_FIELD, true, false, false);
        ensureTextIndex(PROFILE_FIELD, true, false, false);
        ensureTextArrayIndex(FACEBOOK_ACCOUNT_FIELD, true, false);
        ensureTextArrayIndex(TWITTER_ACCOUNT_FIELD, true, false);
        ensureTextArrayIndex(DONATION_FIELD, true, false);
        ensureTextArrayIndex(EMAIL_FIELD, true, false);
        ensureTextArrayIndex(PHONE_FIELD, true, false);
        ensureTextArrayIndex(INTEREST_FIELD, true, false);
        ensureTextIndex(PROFILE_PIC_FIELD, true, false, false);
        ensureTextIndex(MEMBER_FIELD, true, false, false);
        ensureTextIndex(DONOR_FIELD, true, false, false);
        ensureTextIndex(VOTER_ID_FIELD, true, false, false);
        */
        // amazonCloudSearchClient.addDocument(addRequest);
        AmazonCloudSearchDeleteRequest amazonCloudSearchDeleteRequest = new AmazonCloudSearchDeleteRequest();
        amazonCloudSearchDeleteRequest.id = "acb123";
        amazonCloudSearchClient.deleteDocument(amazonCloudSearchDeleteRequest);

        // AmazonCloudSearchClient client = new AmazonCloudSearchClient(awsCredentials);
        // client.setSearchDomain(searchEndpoint);
        // client.setDocumentDomain(documentEndpoint);
        
    }

    private static void ensureTextIndex(String fieldName, boolean returnEnabled, boolean sortEnabled, boolean highlightEnabled) {
        if (indexedField.containsKey(fieldName) && indexedField.get(fieldName).equalsIgnoreCase("text")) {
            return;
        }
        DefineIndexFieldRequest defineIndexFieldRequest = new DefineIndexFieldRequest();
        IndexField indexField = new IndexField();
        indexField.setIndexFieldName(fieldName);
        indexField.setIndexFieldType(IndexFieldType.Text);
        TextOptions textOptions = new TextOptions();
        textOptions.setReturnEnabled(returnEnabled);
        textOptions.setSortEnabled(sortEnabled);
        textOptions.setHighlightEnabled(highlightEnabled);
        indexField.setTextOptions(textOptions);
        defineIndexFieldRequest.setIndexField(indexField);
        defineIndexFieldRequest.setDomainName("sa-users");
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);


    }

    private static void ensureTextArrayIndex(String fieldName, boolean returnEnabled, boolean highlightEnabled) {
        if (indexedField.containsKey(fieldName) && indexedField.get(fieldName).equalsIgnoreCase("text-array")) {
            return;
        }
        DefineIndexFieldRequest defineIndexFieldRequest = new DefineIndexFieldRequest();
        IndexField indexField = new IndexField();
        indexField.setIndexFieldName(fieldName);
        indexField.setIndexFieldType(IndexFieldType.TextArray);
        TextArrayOptions textOptions = new TextArrayOptions();
        textOptions.setReturnEnabled(returnEnabled);
        textOptions.setHighlightEnabled(highlightEnabled);
        indexField.setTextArrayOptions(textOptions);
        defineIndexFieldRequest.setIndexField(indexField);
        defineIndexFieldRequest.setDomainName("sa-users");
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);

    }

    private static void ensureDateIndex(String fieldName, boolean returnEnabled, boolean sortEnabled, boolean facetEnabled) {
        if (indexedField.containsKey(fieldName) && indexedField.get(fieldName).equalsIgnoreCase("date")) {
            return;
        }
        DefineIndexFieldRequest defineIndexFieldRequest = new DefineIndexFieldRequest();
        IndexField indexField = new IndexField();
        indexField.setIndexFieldName(fieldName);
        indexField.setIndexFieldType(IndexFieldType.Date);
        DateOptions dateOptions = new DateOptions();
        dateOptions.setReturnEnabled(returnEnabled);
        dateOptions.setSortEnabled(sortEnabled);
        dateOptions.setFacetEnabled(facetEnabled);
        indexField.setDateOptions(dateOptions);
        defineIndexFieldRequest.setIndexField(indexField);
        defineIndexFieldRequest.setDomainName("sa-users");
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);

    }

    private static void ensureIndex(AmazonCloudSearchClient amazonCloudSearchClient, String fieldName, IndexFieldType indexFieldType) {
        DefineIndexFieldRequest defineIndexFieldRequest = new DefineIndexFieldRequest();

        IndexField indexField = new IndexField();
        indexField.setIndexFieldName(fieldName);
        indexField.setIndexFieldType(indexFieldType);
        TextOptions textOptions = new TextOptions();
        textOptions.setReturnEnabled(true);
        indexField.setTextOptions(textOptions);
        
        defineIndexFieldRequest.setIndexField(indexField);
        defineIndexFieldRequest.setDomainName("sa-users");


        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);

    }

}
