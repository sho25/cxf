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
name|binding
operator|.
name|soap
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|binding
operator|.
name|soap
operator|.
name|Soap12
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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConstants
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|binding
operator|.
name|soap
operator|.
name|model
operator|.
name|SoapOperationInfo
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
name|helpers
operator|.
name|CastUtils
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
name|Fault
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
name|phase
operator|.
name|Phase
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MIME_HEADERS
import|;
end_import

begin_comment
comment|/**  * This interceptor is responsible for setting up the SOAP version  * and header, so that this is available to any pre-protocol interceptors  * that require these to be available.  */
end_comment

begin_class
specifier|public
class|class
name|SoapPreProtocolOutInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|SoapPreProtocolOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mediate a message dispatch.      *       * @param message the current message      * @throws Fault      */
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|ensureVersion
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|ensureMimeHeaders
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|setSoapAction
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Ensure the SOAP version is set for this message.      *       * @param message the current message      */
specifier|private
name|void
name|ensureVersion
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|SoapVersion
name|soapVersion
init|=
name|message
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|soapVersion
operator|==
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|instanceof
name|SoapMessage
condition|)
block|{
name|soapVersion
operator|=
operator|(
operator|(
name|SoapMessage
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|)
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|message
operator|.
name|setVersion
argument_list|(
name|soapVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|soapVersion
operator|==
literal|null
condition|)
block|{
name|soapVersion
operator|=
name|Soap11
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|message
operator|.
name|setVersion
argument_list|(
name|soapVersion
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|soapVersion
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Ensure the SOAP header is set for this message.      *       * @param message the current message      */
specifier|private
name|void
name|ensureMimeHeaders
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|MIME_HEADERS
argument_list|)
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|MIME_HEADERS
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setSoapAction
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|BindingOperationInfo
name|boi
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// The soap action is set on the wrapped operation.
if|if
condition|(
name|boi
operator|!=
literal|null
operator|&&
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
name|String
name|action
init|=
name|getSoapAction
argument_list|(
name|message
argument_list|,
name|boi
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap11
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|reqHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|reqHeaders
operator|==
literal|null
condition|)
block|{
name|reqHeaders
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reqHeaders
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|reqHeaders
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|reqHeaders
operator|.
name|containsKey
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
condition|)
block|{
name|reqHeaders
operator|.
name|put
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
operator|&&
operator|!
literal|"\"\""
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"action=\""
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|ct
operator|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|ct
argument_list|)
operator|.
name|append
argument_list|(
literal|"; action="
argument_list|)
operator|.
name|append
argument_list|(
name|action
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getSoapAction
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|)
block|{
comment|// allow an interceptor to override the SOAPAction if need be
name|String
name|action
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
decl_stmt|;
comment|// Fall back on the SOAPAction in the operation info
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
name|action
operator|=
literal|"\"\""
expr_stmt|;
block|}
else|else
block|{
name|SoapOperationInfo
name|soi
init|=
operator|(
name|SoapOperationInfo
operator|)
name|boi
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|=
name|soi
operator|==
literal|null
condition|?
literal|"\"\""
else|:
name|soi
operator|.
name|getAction
argument_list|()
operator|==
literal|null
condition|?
literal|"\"\""
else|:
name|soi
operator|.
name|getAction
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|action
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|action
operator|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|action
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|action
return|;
block|}
block|}
end_class

end_unit

