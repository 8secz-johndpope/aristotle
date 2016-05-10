package com.aristotle.core.service.aws;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import aws.services.cloudsearchv2.AmazonCloudSearchClient;
import aws.services.cloudsearchv2.documents.AmazonCloudSearchAddRequest;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudsearchv2.model.DateOptions;
import com.amazonaws.services.cloudsearchv2.model.DefineIndexFieldRequest;
import com.amazonaws.services.cloudsearchv2.model.DescribeIndexFieldsRequest;
import com.amazonaws.services.cloudsearchv2.model.DescribeIndexFieldsResult;
import com.amazonaws.services.cloudsearchv2.model.DoubleOptions;
import com.amazonaws.services.cloudsearchv2.model.IndexField;
import com.amazonaws.services.cloudsearchv2.model.IndexFieldStatus;
import com.amazonaws.services.cloudsearchv2.model.IndexFieldType;
import com.amazonaws.services.cloudsearchv2.model.TextArrayOptions;
import com.amazonaws.services.cloudsearchv2.model.TextOptions;
import com.aristotle.core.exception.AppException;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public abstract class AwsCloudBaseSearchService {

    @Value("${aws_region}")
    private String regions;

    @Value("${aws_access_key}")
    private String accessKey;

    @Value("${aws_access_secret}")
    private String accessSecret;

    private String searchEndpoint;
    private String documentEndpoint;
    private String domainName;
    private AmazonCloudSearchClient amazonCloudSearchClient;
    protected DateFormat dateFormat = new ISO8601DateFormat();

    public AwsCloudBaseSearchService(String searchEndpoint, String documentEndpoint, String domainName) {
        this.searchEndpoint = searchEndpoint;
        this.documentEndpoint = documentEndpoint;
        this.domainName = domainName;

    }

    @PostConstruct
    public void init() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, accessSecret);

        amazonCloudSearchClient = new AmazonCloudSearchClient(awsCredentials);
        amazonCloudSearchClient.setEndpoint("cloudsearch.us-west-2.amazonaws.com");// by defaults it goes to East
        amazonCloudSearchClient.setDocumentEndpoint(documentEndpoint);
        amazonCloudSearchClient.setSearchEndpoint(searchEndpoint);
        indexFields();

    }

    private void indexFields() {

        DescribeIndexFieldsRequest describeIndexFieldsRequest = new DescribeIndexFieldsRequest();
        describeIndexFieldsRequest.setDomainName(domainName);

        DescribeIndexFieldsResult describeIndexFieldsResult = getAmazonCloudSearchClient().describeIndexFields(describeIndexFieldsRequest);

        indexedField = new HashMap<String, String>();
        for (IndexFieldStatus indexFieldStatus : describeIndexFieldsResult.getIndexFields()) {
            System.out.println(indexFieldStatus.getOptions().getIndexFieldName() + " : " + indexFieldStatus.getOptions().getIndexFieldType());
            indexedField.put(indexFieldStatus.getOptions().getIndexFieldName(), indexFieldStatus.getOptions().getIndexFieldType());
        }
        ensureFieldIndexes();
    }

    protected abstract void ensureFieldIndexes();

    private Map<String, String> indexedField;

    protected void ensureTextIndex(String fieldName, boolean returnEnabled, boolean sortEnabled, boolean highlightEnabled) {
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
        defineIndexFieldRequest.setDomainName(domainName);
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);

    }

    protected void ensureDoubleIndex(String fieldName, boolean returnEnabled, boolean sortEnabled, boolean highlightEnabled) {
        if (indexedField.containsKey(fieldName) && indexedField.get(fieldName).equalsIgnoreCase("double")) {
            return;
        }
        DefineIndexFieldRequest defineIndexFieldRequest = new DefineIndexFieldRequest();
        IndexField indexField = new IndexField();
        indexField.setIndexFieldName(fieldName);
        indexField.setIndexFieldType(IndexFieldType.Double);
        DoubleOptions doubleOptions = new DoubleOptions();
        doubleOptions.setReturnEnabled(returnEnabled);
        doubleOptions.setSortEnabled(sortEnabled);
        indexField.setDoubleOptions(doubleOptions);
        defineIndexFieldRequest.setIndexField(indexField);
        defineIndexFieldRequest.setDomainName(domainName);
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);

    }

    protected void ensureTextArrayIndex(String fieldName, boolean returnEnabled, boolean highlightEnabled) {
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
        defineIndexFieldRequest.setDomainName(domainName);
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);

    }

    protected void ensureDateIndex(String fieldName, boolean returnEnabled, boolean sortEnabled, boolean facetEnabled) {
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
        defineIndexFieldRequest.setDomainName(domainName);
        amazonCloudSearchClient.defineIndexField(defineIndexFieldRequest);
    }


    public AmazonCloudSearchClient getAmazonCloudSearchClient() {
        return amazonCloudSearchClient;
    }

    protected void indexDocuments(List<AmazonCloudSearchAddRequest> addRequests) throws AppException {
        try {
            amazonCloudSearchClient.addDocuments(addRequests);
        } catch (Exception ex) {
            throw new AppException(ex);
        }
    }

	public String getSearchEndpoint() {
		return searchEndpoint;
	}
}