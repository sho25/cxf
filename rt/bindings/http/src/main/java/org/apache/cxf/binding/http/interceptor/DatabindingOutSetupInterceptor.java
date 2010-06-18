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
name|http
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|Document
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
name|Node
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
name|http
operator|.
name|HttpConstants
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
name|http
operator|.
name|URIMapper
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
name|xml
operator|.
name|interceptor
operator|.
name|XMLMessageOutInterceptor
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
name|common
operator|.
name|WSDLConstants
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
name|endpoint
operator|.
name|Endpoint
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
name|DOMUtils
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
name|MapNamespaceContext
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
name|interceptor
operator|.
name|InterceptorChain
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
name|MessageSenderInterceptor
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
name|StaxOutInterceptor
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
name|WrappedOutInterceptor
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
name|AbstractPhaseInterceptor
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
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|W3CDOMStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|DatabindingOutSetupInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|WrappedOutInterceptor
name|WRAPPED_OUT
init|=
operator|new
name|WrappedOutInterceptor
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|XMLMessageOutInterceptor
name|XML_OUT
init|=
operator|new
name|XMLMessageOutInterceptor
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|StaxOutInterceptor
name|STAX_OUT
init|=
operator|new
name|StaxOutInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|DatabindingOutSetupInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|boolean
name|client
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
decl_stmt|;
name|InterceptorChain
name|chain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
if|if
condition|(
name|client
condition|)
block|{
name|Document
name|document
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|XMLStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|document
argument_list|)
decl_stmt|;
try|try
block|{
name|MapNamespaceContext
name|nsMap
init|=
operator|new
name|MapNamespaceContext
argument_list|()
decl_stmt|;
name|nsMap
operator|.
name|addNamespace
argument_list|(
name|WSDLConstants
operator|.
name|NP_SCHEMA_XSD
argument_list|,
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSD
argument_list|)
expr_stmt|;
name|writer
operator|.
name|setNamespaceContext
argument_list|(
name|nsMap
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|WrappedOutInterceptor
name|wrappedOut
init|=
operator|new
name|WrappedOutInterceptor
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
decl_stmt|;
name|wrappedOut
operator|.
name|addAfter
argument_list|(
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|wrappedOut
argument_list|)
expr_stmt|;
specifier|final
name|XMLMessageOutInterceptor
name|xmlOut
init|=
operator|new
name|XMLMessageOutInterceptor
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
decl_stmt|;
name|xmlOut
operator|.
name|addAfter
argument_list|(
name|wrappedOut
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|xmlOut
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|URIMapper
name|mapper
init|=
operator|(
name|URIMapper
operator|)
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|get
argument_list|(
name|URIMapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|bop
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
name|String
name|verb
init|=
name|mapper
operator|.
name|getVerb
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|verb
argument_list|)
expr_stmt|;
name|boolean
name|putOrPost
init|=
name|verb
operator|.
name|equals
argument_list|(
name|HttpConstants
operator|.
name|POST
argument_list|)
operator|||
name|verb
operator|.
name|equals
argument_list|(
name|HttpConstants
operator|.
name|PUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|putOrPost
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|URIParameterOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
operator|new
name|DocumentWriterInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
operator|new
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
argument_list|(
literal|"remove-writer"
argument_list|,
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
block|{
block|{
name|addAfter
argument_list|(
name|xmlOut
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|MessageSenderInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|message
operator|.
name|removeContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|STAX_OUT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|URIParameterOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|chain
operator|.
name|add
argument_list|(
name|STAX_OUT
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|WRAPPED_OUT
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|XML_OUT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

