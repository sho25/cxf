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
name|Vector
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
specifier|static
specifier|final
name|Logger
name|TIME_LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WSS4JOutInterceptor
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|WSS4JOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"-Time"
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
comment|//must turn off mtom when using WS-Sec so binary is inlined so it can
comment|//be properly signed/encrypted/etc...
name|mc
operator|.
name|put
argument_list|(
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
name|MTOM_ENABLED
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|boolean
name|doTimeDebug
init|=
name|TIME_LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
decl_stmt|;
name|SoapVersion
name|version
init|=
name|mc
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|long
name|t0
init|=
literal|0
decl_stmt|;
name|long
name|t1
init|=
literal|0
decl_stmt|;
name|long
name|t2
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|doTimeDebug
condition|)
block|{
name|t0
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
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
literal|"WSDoAllSender: enter invoke()"
argument_list|)
expr_stmt|;
block|}
name|RequestData
name|reqData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|reqData
operator|.
name|setMsgContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
comment|/*              * The overall try, just to have a finally at the end to perform some              * housekeeping.              */
try|try
block|{
comment|/*                  * Get the action first.                  */
name|Vector
name|actions
init|=
operator|new
name|Vector
argument_list|()
decl_stmt|;
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
name|int
name|doAction
init|=
name|WSSecurityUtil
operator|.
name|decodeAction
argument_list|(
name|action
argument_list|,
name|actions
argument_list|)
decl_stmt|;
if|if
condition|(
name|doAction
operator|==
name|WSConstants
operator|.
name|NO_SECURITY
condition|)
block|{
return|return;
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
comment|/*                  * Now we perform some set-up for UsernameToken and Signature                  * functions. No need to do it for encryption only. Check if                  * username is available and then get a passowrd.                  */
if|if
condition|(
operator|(
name|doAction
operator|&
operator|(
name|WSConstants
operator|.
name|SIGN
operator||
name|WSConstants
operator|.
name|UT
operator||
name|WSConstants
operator|.
name|UT_SIGN
operator|)
operator|)
operator|!=
literal|0
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
condition|)
block|{
comment|/*                      * We need a username - if none throw an SoapFault. For                      * encryption there is a specific parameter to get a username.                      */
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
literal|"Action: "
operator|+
name|doAction
argument_list|)
expr_stmt|;
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
comment|/*                  * Now get the SOAP part from the request message and convert it                  * into a Document. This forces CXF to serialize the SOAP request                  * into FORM_STRING. This string is converted into a document.                  * During the FORM_STRING serialization CXF performs multi-ref of                  * complex data types (if requested), generates and inserts                  * references for attachements and so on. The resulting Document                  * MUST be the complete and final SOAP request as CXF would send it                  * over the wire. Therefore this must shall be the last (or only)                  * handler in a chain. Now we can perform our security operations on                  * this request.                  */
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
comment|/**                  * There is nothing to send...Usually happens when the provider                  * needs to send a HTTP 202 message (with no content)                  */
if|if
condition|(
name|mc
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|doTimeDebug
condition|)
block|{
name|t1
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
name|doSenderAction
argument_list|(
name|doAction
argument_list|,
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
name|doTimeDebug
condition|)
block|{
name|t2
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|TIME_LOG
operator|.
name|fine
argument_list|(
literal|"Send request: total= "
operator|+
operator|(
name|t2
operator|-
name|t0
operator|)
operator|+
literal|" request preparation= "
operator|+
operator|(
name|t1
operator|-
name|t0
operator|)
operator|+
literal|" request processing= "
operator|+
operator|(
name|t2
operator|-
name|t1
operator|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
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
literal|"WSDoAllSender: exit invoke()"
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
operator|.
name|clear
argument_list|()
expr_stmt|;
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
block|}
block|}
end_class

end_unit

