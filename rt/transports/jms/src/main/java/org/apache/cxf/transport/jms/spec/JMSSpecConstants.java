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
name|transport
operator|.
name|jms
operator|.
name|spec
package|;
end_package

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSSpecConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
init|=
literal|"http://www.w3.org/2008/07/"
operator|+
literal|"soap/bindings/JMS/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_JMS_NAMESPACE
init|=
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_JMS_PREFIX
init|=
literal|"SOAPJMS_"
decl_stmt|;
comment|// Connection to a destination properties
comment|// just for jms uri
specifier|public
specifier|static
specifier|final
name|String
name|LOOKUPVARIANT_PARAMETER_NAME
init|=
literal|"lookupVariant"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DESTINATIONNAME_PARAMETER_NAME
init|=
literal|"destinationName"
decl_stmt|;
comment|// other connection destination properties
specifier|public
specifier|static
specifier|final
name|String
name|JNDICONNECTIONFACTORYNAME_PARAMETER_NAME
init|=
literal|"jndiConnectionFactoryName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDIINITIALCONTEXTFACTORY_PARAMETER_NAME
init|=
literal|"jndiInitialContextFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDIURL_PARAMETER_NAME
init|=
literal|"jndiURL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDICONTEXTPARAMETER_PARAMETER_NAME
init|=
literal|"jndiContextParameter"
decl_stmt|;
comment|// JMS Message Header properties
specifier|public
specifier|static
specifier|final
name|String
name|DELIVERYMODE_PARAMETER_NAME
init|=
literal|"deliveryMode"
decl_stmt|;
comment|// Expiration Time
specifier|public
specifier|static
specifier|final
name|String
name|TIMETOLIVE_PARAMETER_NAME
init|=
literal|"timeToLive"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PRIORITY_PARAMETER_NAME
init|=
literal|"priority"
decl_stmt|;
comment|// Destination
specifier|public
specifier|static
specifier|final
name|String
name|REPLYTONAME_PARAMETER_NAME
init|=
literal|"replyToName"
decl_stmt|;
comment|// JMS Message properties' names.
specifier|public
specifier|static
specifier|final
name|String
name|REQUESTURI_PARAMETER_NAME
init|=
literal|"requestURI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BINDINGVERSION_PARAMETER_NAME
init|=
literal|"bindingVersion"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAPACTION_PARAMETER_NAME
init|=
literal|"soapAction"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TARGETSERVICE_PARAMETER_NAME
init|=
literal|"targetService"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENTTYPE_PARAMETER_NAME
init|=
literal|"contentType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ISFAULT_PARAMETER_NAME
init|=
literal|"isFault"
decl_stmt|;
comment|// JMS Field name
specifier|public
specifier|static
specifier|final
name|String
name|REQUESTURI_FIELD
init|=
name|SOAP_JMS_PREFIX
operator|+
name|REQUESTURI_PARAMETER_NAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BINDINGVERSION_FIELD
init|=
name|SOAP_JMS_PREFIX
operator|+
name|BINDINGVERSION_PARAMETER_NAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAPACTION_FIELD
init|=
name|SOAP_JMS_PREFIX
operator|+
name|SOAPACTION_PARAMETER_NAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TARGETSERVICE_FIELD
init|=
name|SOAP_JMS_PREFIX
operator|+
name|TARGETSERVICE_PARAMETER_NAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENTTYPE_FIELD
init|=
name|SOAP_JMS_PREFIX
operator|+
name|CONTENTTYPE_PARAMETER_NAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ISFAULT_FIELD
init|=
name|SOAP_JMS_PREFIX
operator|+
name|ISFAULT_PARAMETER_NAME
decl_stmt|;
comment|//
specifier|public
specifier|static
specifier|final
name|String
name|JMS_MESSAGE_TYPE
init|=
literal|"JMSMessageType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TARGET_SERVICE_IN_REQUESTURI
init|=
literal|"target.service.inrequesturi"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MALFORMED_REQUESTURI
init|=
literal|"malformed.requesturi"
decl_stmt|;
specifier|private
name|JMSSpecConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

