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
name|systest
operator|.
name|ws
operator|.
name|ut
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPHeaderElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|saaj
operator|.
name|SAAJUtils
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
name|phase
operator|.
name|PhaseInterceptor
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|PolicyBasedWSS4JOutInterceptor
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
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_comment
comment|/**  * Cache the first security header and then use it instead of all subsequent security headers, until  * clear() is called.  */
end_comment

begin_class
specifier|public
class|class
name|SecurityHeaderCacheInterceptor
implements|implements
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SEC_HEADER
init|=
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE_NS
argument_list|,
name|WSConstants
operator|.
name|WSSE_LN
argument_list|,
name|WSConstants
operator|.
name|WSSE_PREFIX
argument_list|)
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|afterInterceptors
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|SOAPHeaderElement
name|cachedSecurityHeader
decl_stmt|;
specifier|public
name|SecurityHeaderCacheInterceptor
parameter_list|()
block|{
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
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
name|SoapMessage
name|mc
parameter_list|)
throws|throws
name|Fault
block|{
name|SOAPMessage
name|saaj
init|=
name|mc
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedSecurityHeader
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|cachedHeadersIterator
init|=
name|SAAJUtils
operator|.
name|getHeader
argument_list|(
name|saaj
argument_list|)
operator|.
name|getChildElements
argument_list|(
name|SEC_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedHeadersIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|cachedSecurityHeader
operator|=
operator|(
name|SOAPHeaderElement
operator|)
name|cachedHeadersIterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
else|else
block|{
try|try
block|{
name|saaj
operator|.
name|getSOAPHeader
argument_list|()
operator|.
name|removeContents
argument_list|()
expr_stmt|;
name|SOAPHeaderElement
name|secHeaderElement
init|=
name|SAAJUtils
operator|.
name|getHeader
argument_list|(
name|saaj
argument_list|)
operator|.
name|addHeaderElement
argument_list|(
name|SEC_HEADER
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|cachedHeadersIterator
init|=
name|cachedSecurityHeader
operator|.
name|getChildElements
argument_list|()
decl_stmt|;
while|while
condition|(
name|cachedHeadersIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|secHeaderElement
operator|.
name|addChildElement
argument_list|(
operator|(
name|SOAPElement
operator|)
name|cachedHeadersIterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|cachedSecurityHeader
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|arg0
parameter_list|)
block|{
comment|// Complete
block|}
specifier|public
name|Collection
argument_list|<
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getAdditionalInterceptors
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getAfter
parameter_list|()
block|{
return|return
name|afterInterceptors
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getBefore
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|SecurityHeaderCacheInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPhase
parameter_list|()
block|{
return|return
name|Phase
operator|.
name|PRE_PROTOCOL_ENDING
return|;
block|}
block|}
end_class

end_unit

