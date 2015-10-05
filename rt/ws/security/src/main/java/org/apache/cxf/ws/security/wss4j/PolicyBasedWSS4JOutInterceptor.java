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
name|ws
operator|.
name|security
operator|.
name|wss4j
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Provider
import|;
end_import

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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|SOAPMessage
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
name|attachment
operator|.
name|AttachmentUtil
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
name|SoapFault
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
name|SAAJOutInterceptor
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
name|common
operator|.
name|i18n
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|MessageUtils
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
name|policy
operator|.
name|AssertionInfoMap
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
name|SecurityConstants
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
name|policy
operator|.
name|PolicyUtils
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
name|policyhandlers
operator|.
name|AsymmetricBindingHandler
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
name|policyhandlers
operator|.
name|SymmetricBindingHandler
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
name|policyhandlers
operator|.
name|TransportBindingHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
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
name|crypto
operator|.
name|ThreadLocalSecurityProvider
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
name|dom
operator|.
name|WSSConfig
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
name|handler
operator|.
name|WSHandlerConstants
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
name|message
operator|.
name|WSSecHeader
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
name|policy
operator|.
name|model
operator|.
name|AbstractBinding
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
name|policy
operator|.
name|model
operator|.
name|AsymmetricBinding
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
name|policy
operator|.
name|model
operator|.
name|SymmetricBinding
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
name|policy
operator|.
name|model
operator|.
name|TransportBinding
import|;
end_import

begin_class
specifier|public
class|class
name|PolicyBasedWSS4JOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SECURITY_PROCESSED
init|=
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".DONE"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PolicyBasedWSS4JOutInterceptor
name|INSTANCE
init|=
operator|new
name|PolicyBasedWSS4JOutInterceptor
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PolicyBasedWSS4JOutInterceptorInternal
name|ending
decl_stmt|;
specifier|private
name|SAAJOutInterceptor
name|saajOut
init|=
operator|new
name|SAAJOutInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|PolicyBasedWSS4JOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|SAAJOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ending
operator|=
name|createEndingInterceptor
argument_list|()
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
name|boolean
name|enableStax
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|mc
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|enableStax
condition|)
block|{
if|if
condition|(
name|mc
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
name|saajOut
operator|.
name|handleMessage
argument_list|(
name|mc
argument_list|)
expr_stmt|;
block|}
name|mc
operator|.
name|put
argument_list|(
name|SECURITY_PROCESSED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|mc
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|saajOut
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|PolicyBasedWSS4JOutInterceptorInternal
name|createEndingInterceptor
parameter_list|()
block|{
return|return
operator|new
name|PolicyBasedWSS4JOutInterceptorInternal
argument_list|()
return|;
block|}
specifier|public
specifier|final
class|class
name|PolicyBasedWSS4JOutInterceptorInternal
implements|implements
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
name|PolicyBasedWSS4JOutInterceptorInternal
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
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
name|Object
name|provider
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|useCustomProvider
init|=
name|provider
operator|!=
literal|null
operator|&&
name|ThreadLocalSecurityProvider
operator|.
name|isInstalled
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|useCustomProvider
condition|)
block|{
name|ThreadLocalSecurityProvider
operator|.
name|setProvider
argument_list|(
operator|(
name|Provider
operator|)
name|provider
argument_list|)
expr_stmt|;
block|}
name|handleMessageInternal
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|useCustomProvider
condition|)
block|{
name|ThreadLocalSecurityProvider
operator|.
name|unsetProvider
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|handleMessageInternal
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aim
operator|==
literal|null
condition|)
block|{
comment|// no policies available
return|return;
block|}
name|SOAPMessage
name|saaj
init|=
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|mustUnderstand
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|MUST_UNDERSTAND
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|actor
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ACTOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|AttachmentUtil
operator|.
name|isMtomEnabled
argument_list|(
name|message
argument_list|)
operator|&&
name|hasAttachments
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"MTOM is enabled with WS-Security. Please note that if an attachment is "
operator|+
literal|"referenced in the SOAP Body, only the reference will be signed and not the "
operator|+
literal|"SOAP Body!"
argument_list|)
expr_stmt|;
block|}
comment|// extract Assertion information
name|AbstractBinding
name|binding
init|=
name|PolicyUtils
operator|.
name|getSecurityBinding
argument_list|(
name|aim
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
operator|&&
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|Policy
name|policy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|binding
operator|=
operator|new
name|TransportBinding
argument_list|(
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP11
argument_list|,
name|policy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|!=
literal|null
condition|)
block|{
name|WSSecHeader
name|secHeader
init|=
operator|new
name|WSSecHeader
argument_list|(
name|actor
argument_list|,
name|mustUnderstand
argument_list|,
name|saaj
operator|.
name|getSOAPPart
argument_list|()
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
literal|null
decl_stmt|;
try|try
block|{
name|el
operator|=
name|secHeader
operator|.
name|insertSecurityHeader
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SECURITY_FAILED"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
try|try
block|{
comment|//move to end
name|SAAJUtils
operator|.
name|getHeader
argument_list|(
name|saaj
argument_list|)
operator|.
name|removeChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|SAAJUtils
operator|.
name|getHeader
argument_list|(
name|saaj
argument_list|)
operator|.
name|appendChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|WSSConfig
name|config
init|=
operator|(
name|WSSConfig
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|WSSConfig
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
expr_stmt|;
block|}
name|translateProperties
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|String
name|asymSignatureAlgorithm
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ASYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|asymSignatureAlgorithm
operator|!=
literal|null
operator|&&
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setAsymmetricSignature
argument_list|(
name|asymSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
name|String
name|symSignatureAlgorithm
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|symSignatureAlgorithm
operator|!=
literal|null
operator|&&
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setSymmetricSignature
argument_list|(
name|symSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|binding
operator|instanceof
name|TransportBinding
condition|)
block|{
operator|new
name|TransportBindingHandler
argument_list|(
name|config
argument_list|,
operator|(
name|TransportBinding
operator|)
name|binding
argument_list|,
name|saaj
argument_list|,
name|secHeader
argument_list|,
name|aim
argument_list|,
name|message
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|binding
operator|instanceof
name|SymmetricBinding
condition|)
block|{
operator|new
name|SymmetricBindingHandler
argument_list|(
name|config
argument_list|,
operator|(
name|SymmetricBinding
operator|)
name|binding
argument_list|,
name|saaj
argument_list|,
name|secHeader
argument_list|,
name|aim
argument_list|,
name|message
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
else|else
block|{
operator|new
name|AsymmetricBindingHandler
argument_list|(
name|config
argument_list|,
operator|(
name|AsymmetricBinding
operator|)
name|binding
argument_list|,
name|saaj
argument_list|,
name|secHeader
argument_list|,
name|aim
argument_list|,
name|message
argument_list|)
operator|.
name|handleBinding
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
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SECURITY_FAILED"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|el
operator|.
name|getFirstChild
argument_list|()
operator|==
literal|null
condition|)
block|{
name|el
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|hasAttachments
parameter_list|(
name|SoapMessage
name|mc
parameter_list|)
block|{
specifier|final
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|attachments
init|=
name|mc
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
return|return
name|attachments
operator|!=
literal|null
operator|&&
name|attachments
operator|.
name|size
argument_list|()
operator|>
literal|0
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
name|Collections
operator|.
name|emptySet
argument_list|()
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
name|PolicyBasedWSS4JOutInterceptorInternal
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
name|POST_PROTOCOL
return|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
comment|//nothing
block|}
specifier|public
name|Collection
argument_list|<
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
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
specifier|private
name|void
name|translateProperties
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
block|{
name|String
name|bspCompliant
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|)
decl_stmt|;
if|if
condition|(
name|bspCompliant
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
name|bspCompliant
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

