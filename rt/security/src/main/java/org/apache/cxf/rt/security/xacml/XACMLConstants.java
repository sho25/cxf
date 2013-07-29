begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|xacml
package|;
end_package

begin_comment
comment|/**  * XACML 1.x and 2.0 Constants.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|XACMLConstants
block|{
comment|//
comment|// Attributes
comment|//
specifier|public
specifier|static
specifier|final
name|String
name|CURRENT_TIME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:environment:current-time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CURRENT_DATE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:environment:current-date"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CURRENT_DATETIME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:environment:current-dateTime"
decl_stmt|;
comment|//
comment|// Identifiers
comment|//
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_DNS_NAME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_IP_ADDR
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_AUTHN_METHOD
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:authentication-method"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_AUTHN_TIME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:authentication-time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_KEY_INFO
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:key-info"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_REQ_TIME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:request-time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_START_TIME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:session-start-time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_ID
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:subject-id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_ID_QUALIFIER
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject:subject-id-qualifier"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CAT_ACCESS_SUBJECT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CAT_CODEBASE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject-category:codebase"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CAT_INTERMED_SUBJECT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject-category:intermediary-subject"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CAT_REC_SUBJECT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CAT_REQ_MACHINE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_LOC
init|=
literal|"urn:oasis:names:tc:xacml:1.0:resource:resource-location"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_ID
init|=
literal|"urn:oasis:names:tc:xacml:1.0:resource:resource-id"
decl_stmt|;
comment|// Non-standard (CXF-specific) tags for sending information about SOAP services to the PDP
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_WSDL_OPERATION_ID
init|=
literal|"urn:cxf:apache:org:wsdl:operation-id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_WSDL_SERVICE_ID
init|=
literal|"urn:cxf:apache:org:wsdl:service-id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_WSDL_URI_ID
init|=
literal|"urn:cxf:apache:org:wsdl:resource-id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_FILE_NAME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:resource:simple-file-name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_ID
init|=
literal|"urn:oasis:names:tc:xacml:1.0:action:action-id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_IMPLIED
init|=
literal|"urn:oasis:names:tc:xacml:1.0:action:implied-action"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_ROLE
init|=
literal|"urn:oasis:names:tc:xacml:2.0:subject:role"
decl_stmt|;
comment|//
comment|// Datatypes
comment|//
specifier|public
specifier|static
specifier|final
name|String
name|XS_STRING
init|=
literal|"http://www.w3.org/2001/XMLSchema#string"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_BOOLEAN
init|=
literal|"http://www.w3.org/2001/XMLSchema#boolean"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_INT
init|=
literal|"http://www.w3.org/2001/XMLSchema#integer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_DOUBLE
init|=
literal|"http://www.w3.org/2001/XMLSchema#double"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_TIME
init|=
literal|"http://www.w3.org/2001/XMLSchema#time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_DATE
init|=
literal|"http://www.w3.org/2001/XMLSchema#date"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_DATETIME
init|=
literal|"http://www.w3.org/2001/XMLSchema#dateTime"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_ANY_URI
init|=
literal|"http://www.w3.org/2001/XMLSchema#anyURI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_HEX
init|=
literal|"http://www.w3.org/2001/XMLSchema#hexBinary"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XS_BASE64
init|=
literal|"http://www.w3.org/2001/XMLSchema#base64Binary"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RFC_822_NAME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X500_NAME
init|=
literal|"urn:oasis:names:tc:xacml:1.0:data-type:x500Name"
decl_stmt|;
comment|//
comment|// Functions
comment|//
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_STRING_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:string-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_BOOL_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:boolean-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_INT_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:integer-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DOUBLE_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:double-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATE_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:date-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_TIME_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:time-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATETIME_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:dateTime-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_ANY_URI_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_X500_NAME_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:x500Name-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_RFC_822_NAME_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:rfc822Name-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_HEX_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:hexBinary-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_BASE64_EQUAL
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:base64Binary-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_INT_GT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:integer-greater-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_INT_GTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:integer-greater-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_INT_LT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:integer-less-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_INT_LTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:integer-less-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DOUBLE_GT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:double-greater-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DOUBLE_GTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:double-greater-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DOUBLE_LT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:double-less-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DOUBLE_LTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:double-less-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_STRING_GT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:string-greater-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_STRING_GTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:string-greater-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_STRING_LT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:string-less-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_STRING_LTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:string-less-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_TIME_GT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:time-greater-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_TIME_GTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:time-greater-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_TIME_LT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:time-less-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_TIME_LTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:time-less-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATETIME_GT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATETIME_GTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATETIME_LT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATETIME_LTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATE_GT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:date-greater-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATE_GTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATE_LT
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:date-less-than"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FUNC_DATE_LTE
init|=
literal|"urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal"
decl_stmt|;
specifier|private
name|XACMLConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

