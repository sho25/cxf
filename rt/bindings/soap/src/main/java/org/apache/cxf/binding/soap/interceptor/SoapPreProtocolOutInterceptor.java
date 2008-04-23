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
name|interceptor
operator|.
name|AttachmentOutInterceptor
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
name|phase
operator|.
name|Phase
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
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
block|}
end_class

end_unit

