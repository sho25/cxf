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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|security
operator|.
name|SAMLSecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_class
specifier|public
class|class
name|CXFMessageParser
block|{
specifier|private
name|Message
name|message
decl_stmt|;
comment|/**      * @param message      */
specifier|public
name|CXFMessageParser
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSOAPService
parameter_list|()
block|{
return|return
operator|!
operator|(
name|getWSDLService
argument_list|()
operator|==
literal|null
operator|||
name|getWSDLOperation
argument_list|()
operator|==
literal|null
operator|)
return|;
block|}
specifier|public
name|QName
name|getWSDLOperation
parameter_list|()
block|{
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|QName
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getWSDLService
parameter_list|()
block|{
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_SERVICE
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|QName
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_SERVICE
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @param fullRequestURL Whether to send the full Request URL as the resource or not. If set to true, the      *        full Request URL will be sent for both a JAX-WS and JAX-RS service. If set to false (the      *        default), a JAX-WS service will send the "{namespace}operation" QName, and a JAX-RS service      *        will send the RequestURI (i.e. minus the initial https:<ip> prefix)      */
specifier|public
name|String
name|getResourceURI
parameter_list|(
name|boolean
name|fullRequestURL
parameter_list|)
block|{
name|String
name|property
init|=
name|fullRequestURL
condition|?
name|Message
operator|.
name|REQUEST_URL
else|:
name|Message
operator|.
name|REQUEST_URI
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|get
argument_list|(
name|property
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|property
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getAction
parameter_list|(
name|String
name|defaultSOAPAction
parameter_list|)
block|{
name|String
name|actionToUse
init|=
name|defaultSOAPAction
decl_stmt|;
comment|// For REST use the HTTP Verb
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|==
literal|null
operator|&&
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|actionToUse
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
block|}
return|return
name|actionToUse
return|;
block|}
comment|/**      * Get the Issuer of the SAML Assertion      */
specifier|public
name|String
name|getIssuer
parameter_list|()
throws|throws
name|WSSecurityException
block|{
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|instanceof
name|SAMLSecurityContext
condition|)
block|{
name|Element
name|assertionElement
init|=
operator|(
operator|(
name|SAMLSecurityContext
operator|)
name|sc
operator|)
operator|.
name|getAssertionElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|assertionElement
operator|!=
literal|null
condition|)
block|{
name|SamlAssertionWrapper
name|wrapper
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|assertionElement
argument_list|)
decl_stmt|;
return|return
name|wrapper
operator|.
name|getIssuerString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

