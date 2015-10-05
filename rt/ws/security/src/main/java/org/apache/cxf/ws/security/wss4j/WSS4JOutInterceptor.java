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
name|Level
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
name|Document
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
name|WSConstants
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
name|action
operator|.
name|Action
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
name|HandlerAction
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
name|RequestData
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
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_class
specifier|public
class|class
name|WSS4JOutInterceptor
extends|extends
name|AbstractWSS4JInterceptor
block|{
comment|/**      * Property name for a map of action IDs ({@link Integer}) to action      * class names. Values can be either {@link Class}) or Objects -    * implementing {@link Action}.      */
specifier|public
specifier|static
specifier|final
name|String
name|WSS4J_ACTION_MAP
init|=
literal|"wss4j.action.map"
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
name|WSS4JOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|WSS4JOutInterceptorInternal
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
specifier|private
name|boolean
name|mtomEnabled
decl_stmt|;
specifier|public
name|WSS4JOutInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setPhase
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
name|WSS4JOutInterceptor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isAllowMTOM
parameter_list|()
block|{
return|return
name|mtomEnabled
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|void
name|setAllowMTOM
parameter_list|(
name|boolean
name|allowMTOM
parameter_list|)
block|{
name|this
operator|.
name|mtomEnabled
operator|=
name|allowMTOM
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getProperty
parameter_list|(
name|Object
name|msgContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
comment|// use the superclass first
name|Object
name|result
init|=
name|super
operator|.
name|getProperty
argument_list|(
name|msgContext
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|// handle the special case of the RECV_RESULTS
if|if
condition|(
name|result
operator|==
literal|null
operator|&&
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|this
operator|.
name|isRequestor
argument_list|(
operator|(
name|SoapMessage
operator|)
name|msgContext
argument_list|)
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|SoapMessage
operator|)
name|msgContext
operator|)
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
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
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
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
name|WSS4JOutInterceptorInternal
name|createEndingInterceptor
parameter_list|()
block|{
return|return
operator|new
name|WSS4JOutInterceptorInternal
argument_list|()
return|;
block|}
specifier|final
class|class
name|WSS4JOutInterceptorInternal
implements|implements
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
name|WSS4JOutInterceptorInternal
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
name|mc
parameter_list|)
throws|throws
name|Fault
block|{
name|boolean
name|doDebug
init|=
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
decl_stmt|;
if|if
condition|(
name|doDebug
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"WSS4JOutInterceptor: enter handleMessage()"
argument_list|)
expr_stmt|;
block|}
comment|/**              * There is nothing to send...Usually happens when the provider              * needs to send a HTTP 202 message (with no content)              */
if|if
condition|(
name|mc
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|SoapVersion
name|version
init|=
name|mc
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|RequestData
name|reqData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
comment|/*              * The overall try, just to have a finally at the end to perform some              * housekeeping.              */
try|try
block|{
name|WSSConfig
name|config
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
name|reqData
operator|.
name|setWssConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
comment|/*                  * Setup any custom actions first by processing the input properties                  * and reconfiguring the WSSConfig with the user defined properties.                  */
name|this
operator|.
name|configureActions
argument_list|(
name|mc
argument_list|,
name|doDebug
argument_list|,
name|version
argument_list|,
name|config
argument_list|)
expr_stmt|;
comment|/*                  * Get the action first.                  */
name|List
argument_list|<
name|HandlerAction
argument_list|>
name|actions
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|mc
argument_list|,
name|WSHandlerConstants
operator|.
name|HANDLER_ACTIONS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|actions
operator|==
literal|null
condition|)
block|{
comment|// If null then just fall back to the "action" String
name|String
name|action
init|=
name|getString
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|mc
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_ACTION"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
name|actions
operator|=
name|WSSecurityUtil
operator|.
name|decodeHandlerAction
argument_list|(
name|action
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|actions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|translateProperties
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|reqData
operator|.
name|setMsgContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|reqData
operator|.
name|setAttachmentCallbackHandler
argument_list|(
operator|new
name|AttachmentCallbackHandler
argument_list|(
name|mc
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|AttachmentUtil
operator|.
name|isMtomEnabled
argument_list|(
name|mc
argument_list|)
operator|&&
name|hasAttachments
argument_list|(
name|mc
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
comment|/*                  * For every action we need a username, so get this now. The                  * username defined in the deployment descriptor takes precedence.                  */
name|reqData
operator|.
name|setUsername
argument_list|(
operator|(
name|String
operator|)
name|getOption
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|reqData
operator|.
name|getUsername
argument_list|()
operator|==
literal|null
operator|||
name|reqData
operator|.
name|getUsername
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|String
name|username
init|=
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|reqData
operator|.
name|getMsgContext
argument_list|()
argument_list|,
name|WSHandlerConstants
operator|.
name|USER
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|reqData
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Check to see if we require a username (+ if it's missing)
name|boolean
name|userNameRequired
init|=
literal|false
decl_stmt|;
for|for
control|(
name|HandlerAction
name|handlerAction
range|:
name|actions
control|)
block|{
if|if
condition|(
operator|(
name|handlerAction
operator|.
name|getAction
argument_list|()
operator|==
name|WSConstants
operator|.
name|SIGN
operator|||
name|handlerAction
operator|.
name|getAction
argument_list|()
operator|==
name|WSConstants
operator|.
name|UT
operator|||
name|handlerAction
operator|.
name|getAction
argument_list|()
operator|==
name|WSConstants
operator|.
name|UT_SIGN
operator|)
operator|&&
operator|(
name|handlerAction
operator|.
name|getActionToken
argument_list|()
operator|==
literal|null
operator|||
name|handlerAction
operator|.
name|getActionToken
argument_list|()
operator|.
name|getUser
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
name|userNameRequired
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|userNameRequired
operator|&&
operator|(
name|reqData
operator|.
name|getUsername
argument_list|()
operator|==
literal|null
operator|||
name|reqData
operator|.
name|getUsername
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|)
operator|&&
operator|(
name|String
operator|)
name|getOption
argument_list|(
name|WSHandlerConstants
operator|.
name|SIGNATURE_USER
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_USERNAME"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|doDebug
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Actor: "
operator|+
name|reqData
operator|.
name|getActor
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*                  * Now get the SOAP part from the request message and convert it                  * into a Document. This forces CXF to serialize the SOAP request                  * into FORM_STRING. This string is converted into a document.                  * During the FORM_STRING serialization CXF performs multi-ref of                  * complex data types (if requested), generates and inserts                  * references for attachments and so on. The resulting Document                  * MUST be the complete and final SOAP request as CXF would send it                  * over the wire. Therefore this must shall be the last (or only)                  * handler in a chain. Now we can perform our security operations on                  * this request.                  */
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
name|saaj
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"SAAJOutHandler must be enabled for WS-Security!"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_SAAJ_DOC"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
name|Document
name|doc
init|=
name|saaj
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|doSenderAction
argument_list|(
name|doc
argument_list|,
name|reqData
argument_list|,
name|actions
argument_list|,
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|getProperty
argument_list|(
name|mc
argument_list|,
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
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|doDebug
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"WSS4JOutInterceptor: exit handleMessage()"
argument_list|)
expr_stmt|;
block|}
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
name|version
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
name|reqData
operator|=
literal|null
expr_stmt|;
block|}
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
name|WSS4JOutInterceptorInternal
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
specifier|private
name|void
name|configureActions
parameter_list|(
name|SoapMessage
name|mc
parameter_list|,
name|boolean
name|doDebug
parameter_list|,
name|SoapVersion
name|version
parameter_list|,
name|WSSConfig
name|config
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|actionMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|mc
argument_list|,
name|WSS4J_ACTION_MAP
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|actionMap
operator|!=
literal|null
operator|&&
operator|!
name|actionMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|actionMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|removedAction
init|=
literal|null
decl_stmt|;
comment|// Be defensive here since the cast above is slightly risky
comment|// with the handler config options not being strongly typed.
try|try
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|removedAction
operator|=
name|config
operator|.
name|setAction
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Action
condition|)
block|{
name|removedAction
operator|=
name|config
operator|.
name|setAction
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|,
operator|(
name|Action
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"BAD_ACTION"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
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
literal|"BAD_ACTION"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|doDebug
condition|)
block|{
if|if
condition|(
name|removedAction
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Replaced Action: "
operator|+
name|removedAction
operator|.
name|getName
argument_list|()
operator|+
literal|" with Action: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|" for ID: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Added Action: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|" with ID: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
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
block|}
block|}
end_class

end_unit

