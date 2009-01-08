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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|SOAPBody
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamConstants
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
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|SAAJInInterceptor
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
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
name|tokenstore
operator|.
name|SecurityToken
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
name|tokenstore
operator|.
name|TokenStore
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
name|WSPasswordCallback
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
name|WSSConfig
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
name|WSSecurityEngine
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
name|WSSecurityEngineResult
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
name|components
operator|.
name|crypto
operator|.
name|Crypto
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
name|handler
operator|.
name|WSHandlerResult
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
name|message
operator|.
name|token
operator|.
name|SecurityTokenReference
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
name|message
operator|.
name|token
operator|.
name|Timestamp
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
name|processor
operator|.
name|Processor
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

begin_comment
comment|/**  * Performs WS-Security inbound actions.  *   * @author<a href="mailto:tsztelak@gmail.com">Tomasz Sztelak</a>  */
end_comment

begin_class
specifier|public
class|class
name|WSS4JInInterceptor
extends|extends
name|AbstractWSS4JInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_RESULT
init|=
literal|"wss4j.timestamp.result"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_RESULT
init|=
literal|"wss4j.signature.result"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PRINCIPAL_RESULT
init|=
literal|"wss4j.principal.result"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROCESSOR_MAP
init|=
literal|"wss4j.processor.map"
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
name|WSS4JInInterceptor
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
name|WSS4JInInterceptor
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|WSS4JInInterceptor
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
name|SAAJInInterceptor
name|saajIn
init|=
operator|new
name|SAAJInInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|ignoreActions
decl_stmt|;
comment|/**      *      */
specifier|private
name|WSSecurityEngine
name|secEngineOverride
decl_stmt|;
specifier|public
name|WSS4JInInterceptor
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
name|SAAJInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSS4JInInterceptor
parameter_list|(
name|boolean
name|ignore
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|ignoreActions
operator|=
name|ignore
expr_stmt|;
block|}
specifier|public
name|WSS4JInInterceptor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|properties
operator|.
name|get
argument_list|(
name|PROCESSOR_MAP
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|secEngineOverride
operator|=
name|createSecurityEngine
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setIgnoreActions
parameter_list|(
name|boolean
name|i
parameter_list|)
block|{
name|ignoreActions
operator|=
name|i
expr_stmt|;
block|}
specifier|private
name|SOAPMessage
name|getSOAPMessage
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
block|{
name|SOAPMessage
name|doc
init|=
name|msg
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
name|doc
operator|==
literal|null
condition|)
block|{
name|saajIn
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|doc
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
throws|throws
name|Fault
block|{
name|SOAPMessage
name|doc
init|=
name|getSOAPMessage
argument_list|(
name|msg
argument_list|)
decl_stmt|;
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
name|doTimeLog
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
name|msg
operator|.
name|getVersion
argument_list|()
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
literal|"WSS4JInInterceptor: enter handleMessage()"
argument_list|)
expr_stmt|;
block|}
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
name|long
name|t3
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|doTimeLog
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
name|RequestData
name|reqData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
comment|/*          * The overall try, just to have a finally at the end to perform some          * housekeeping.          */
try|try
block|{
name|reqData
operator|.
name|setMsgContext
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|checkPolicies
argument_list|(
name|msg
argument_list|,
name|reqData
argument_list|)
expr_stmt|;
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
name|getAction
argument_list|(
name|msg
argument_list|,
name|version
argument_list|)
decl_stmt|;
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
name|String
name|actor
init|=
operator|(
name|String
operator|)
name|getOption
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTOR
argument_list|)
decl_stmt|;
name|CallbackHandler
name|cbHandler
init|=
name|getCallback
argument_list|(
name|reqData
argument_list|,
name|doAction
argument_list|)
decl_stmt|;
comment|/*              * Get and check the Signature specific parameters first because              * they may be used for encryption too.              */
name|doReceiverAction
argument_list|(
name|doAction
argument_list|,
name|reqData
argument_list|)
expr_stmt|;
name|Vector
name|wsResult
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|doTimeLog
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
name|wsResult
operator|=
name|getSecurityEngine
argument_list|()
operator|.
name|processSecurityHeader
argument_list|(
name|doc
operator|.
name|getSOAPPart
argument_list|()
argument_list|,
name|actor
argument_list|,
name|cbHandler
argument_list|,
name|reqData
operator|.
name|getSigCrypto
argument_list|()
argument_list|,
name|reqData
operator|.
name|getDecCrypto
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|doTimeLog
condition|)
block|{
name|t2
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|wsResult
operator|==
literal|null
condition|)
block|{
comment|// no security header found
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
elseif|else
if|if
condition|(
name|doc
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|hasFault
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Request does not contain required Security header, "
operator|+
literal|"but it's a fault."
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Request does not contain required Security header"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|INVALID_SECURITY
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|reqData
operator|.
name|getWssConfig
argument_list|()
operator|.
name|isEnableSignatureConfirmation
argument_list|()
condition|)
block|{
name|checkSignatureConfirmation
argument_list|(
name|reqData
argument_list|,
name|wsResult
argument_list|)
expr_stmt|;
block|}
comment|/*              * Now we can check the certificate used to sign the message. In the              * following implementation the certificate is only trusted if              * either it itself or the certificate of the issuer is installed in              * the keystore. Note: the method verifyTrust(X509Certificate)              * allows custom implementations with other validation algorithms              * for subclasses.              */
comment|// Extract the signature action result from the action vector
name|WSSecurityEngineResult
name|actionResult
init|=
name|WSSecurityUtil
operator|.
name|fetchActionResult
argument_list|(
name|wsResult
argument_list|,
name|WSConstants
operator|.
name|SIGN
argument_list|)
decl_stmt|;
if|if
condition|(
name|actionResult
operator|!=
literal|null
condition|)
block|{
name|X509Certificate
name|returnCert
init|=
operator|(
name|X509Certificate
operator|)
name|actionResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnCert
operator|!=
literal|null
operator|&&
operator|!
name|verifyTrust
argument_list|(
name|returnCert
argument_list|,
name|reqData
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The certificate used for the signature is not trusted"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_CHECK
argument_list|)
throw|;
block|}
name|msg
operator|.
name|put
argument_list|(
name|SIGNATURE_RESULT
argument_list|,
name|actionResult
argument_list|)
expr_stmt|;
block|}
comment|/*              * Perform further checks on the timestamp that was transmitted in              * the header. In the following implementation the timestamp is              * valid if it was created after (now-ttl), where ttl is set on              * server side, not by the client. Note: the method              * verifyTimestamp(Timestamp) allows custom implementations with              * other validation algorithms for subclasses.              */
comment|// Extract the timestamp action result from the action vector
name|actionResult
operator|=
name|WSSecurityUtil
operator|.
name|fetchActionResult
argument_list|(
name|wsResult
argument_list|,
name|WSConstants
operator|.
name|TS
argument_list|)
expr_stmt|;
if|if
condition|(
name|actionResult
operator|!=
literal|null
condition|)
block|{
name|Timestamp
name|timestamp
init|=
operator|(
name|Timestamp
operator|)
name|actionResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TIMESTAMP
argument_list|)
decl_stmt|;
if|if
condition|(
name|timestamp
operator|!=
literal|null
operator|&&
operator|!
name|verifyTimestamp
argument_list|(
name|timestamp
argument_list|,
name|decodeTimeToLive
argument_list|(
name|reqData
argument_list|)
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The timestamp could not be validated"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|MESSAGE_EXPIRED
argument_list|)
throw|;
block|}
name|msg
operator|.
name|put
argument_list|(
name|TIMESTAMP_RESULT
argument_list|,
name|actionResult
argument_list|)
expr_stmt|;
block|}
comment|/*              * now check the security actions: do they match, in right order?              */
if|if
condition|(
operator|!
name|ignoreActions
operator|&&
operator|!
name|checkReceiverResults
argument_list|(
name|wsResult
argument_list|,
name|actions
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Security processing failed (actions mismatch)"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|INVALID_SECURITY
argument_list|)
throw|;
block|}
name|doResults
argument_list|(
name|msg
argument_list|,
name|actor
argument_list|,
name|doc
argument_list|,
name|wsResult
argument_list|)
expr_stmt|;
if|if
condition|(
name|doTimeLog
condition|)
block|{
name|t3
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
literal|"Receive request: total= "
operator|+
operator|(
name|t3
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
literal|" header, cert verify, timestamp= "
operator|+
operator|(
name|t3
operator|-
name|t2
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
literal|"WSS4JInInterceptor: exit handleMessage()"
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|SoapFault
name|fault
init|=
name|createSoapFault
argument_list|(
name|version
argument_list|,
name|e
argument_list|)
decl_stmt|;
throw|throw
name|fault
throw|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
literal|"STAX_EX"
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
literal|"SAAJ_EX"
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
specifier|private
name|void
name|doResults
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|String
name|actor
parameter_list|,
name|SOAPMessage
name|doc
parameter_list|,
name|Vector
name|wsResult
parameter_list|)
throws|throws
name|SOAPException
throws|,
name|XMLStreamException
block|{
comment|/*          * All ok up to this point. Now construct and setup the security result          * structure. The service may fetch this and check it.          */
name|List
argument_list|<
name|Object
argument_list|>
name|results
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|msg
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
name|results
operator|=
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
name|WSHandlerResult
name|rResult
init|=
operator|new
name|WSHandlerResult
argument_list|(
name|actor
argument_list|,
name|wsResult
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|rResult
argument_list|)
expr_stmt|;
name|SOAPBody
name|body
init|=
name|doc
operator|.
name|getSOAPBody
argument_list|()
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|body
argument_list|)
argument_list|)
decl_stmt|;
comment|// advance just past body
name|int
name|evt
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
operator|&&
name|i
operator|<
literal|1
operator|&&
operator|(
name|evt
operator|!=
name|XMLStreamConstants
operator|.
name|END_ELEMENT
operator|||
name|evt
operator|!=
name|XMLStreamConstants
operator|.
name|START_ELEMENT
operator|)
condition|)
block|{
name|reader
operator|.
name|next
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|msg
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|o
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsResult
argument_list|,
name|WSSecurityEngineResult
operator|.
name|class
argument_list|)
control|)
block|{
specifier|final
name|Principal
name|p
init|=
operator|(
name|Principal
operator|)
name|o
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PRINCIPAL
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|PRINCIPAL_RESULT
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|SecurityContext
name|sc
init|=
name|msg
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
operator|==
literal|null
operator|||
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|SecurityContext
name|c
init|=
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|p
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
specifier|private
name|String
name|getAction
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|SoapVersion
name|version
parameter_list|)
block|{
name|String
name|action
init|=
operator|(
name|String
operator|)
name|getOption
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|action
operator|=
operator|(
name|String
operator|)
name|msg
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No security action was defined!"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SoapFault
argument_list|(
literal|"No security action was defined!"
argument_list|,
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|action
return|;
block|}
specifier|private
class|class
name|TokenStoreCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|CallbackHandler
name|internal
decl_stmt|;
specifier|private
name|TokenStore
name|store
decl_stmt|;
specifier|public
name|TokenStoreCallbackHandler
parameter_list|(
name|CallbackHandler
name|in
parameter_list|,
name|TokenStore
name|st
parameter_list|)
block|{
name|internal
operator|=
name|in
expr_stmt|;
name|store
operator|=
name|st
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|String
name|id
init|=
name|pc
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|pc
operator|.
name|getKeyType
argument_list|()
operator|.
name|equals
argument_list|(
name|SecurityTokenReference
operator|.
name|ENC_KEY_SHA1_URI
argument_list|)
condition|)
block|{
for|for
control|(
name|SecurityToken
name|token
range|:
name|store
operator|.
name|getValidTokens
argument_list|()
control|)
block|{
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getSHA1
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setKey
argument_list|(
name|token
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
else|else
block|{
name|SecurityToken
name|tok
init|=
name|store
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|tok
operator|!=
literal|null
condition|)
block|{
name|pc
operator|.
name|setKey
argument_list|(
name|tok
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
name|internal
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|CallbackHandler
name|getCallback
parameter_list|(
name|RequestData
name|reqData
parameter_list|,
name|int
name|doAction
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|/*          * To check a UsernameToken or to decrypt an encrypted message we need a          * password.          */
name|CallbackHandler
name|cbHandler
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|doAction
operator|&
operator|(
name|WSConstants
operator|.
name|ENCR
operator||
name|WSConstants
operator|.
name|UT
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
name|Object
name|o
init|=
operator|(
operator|(
name|SoapMessage
operator|)
name|reqData
operator|.
name|getMsgContext
argument_list|()
operator|)
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|o
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|o
operator|instanceof
name|CallbackHandler
condition|)
block|{
name|cbHandler
operator|=
operator|(
name|CallbackHandler
operator|)
name|o
expr_stmt|;
block|}
if|if
condition|(
name|cbHandler
operator|==
literal|null
condition|)
block|{
name|cbHandler
operator|=
name|getPasswordCB
argument_list|(
name|reqData
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cbHandler
operator|!=
literal|null
condition|)
block|{
name|Endpoint
name|ep
init|=
operator|(
operator|(
name|SoapMessage
operator|)
name|reqData
operator|.
name|getMsgContext
argument_list|()
operator|)
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
if|if
condition|(
name|ep
operator|!=
literal|null
operator|&&
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|TokenStore
name|store
init|=
operator|(
name|TokenStore
operator|)
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|store
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|TokenStoreCallbackHandler
argument_list|(
name|cbHandler
argument_list|,
name|store
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|cbHandler
return|;
block|}
specifier|public
name|Crypto
name|loadSignatureCrypto
parameter_list|(
name|RequestData
name|reqData
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
return|return
name|super
operator|.
name|loadSignatureCrypto
argument_list|(
name|reqData
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|Crypto
name|loadDecryptionCrypto
parameter_list|(
name|RequestData
name|reqData
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
return|return
name|super
operator|.
name|loadDecryptionCrypto
argument_list|(
name|reqData
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * @return      the WSSecurityEngine in use by this interceptor.      *              This engine is defined to be the secEngineOverride      *              instance, if defined in this class (and supplied through      *              construction); otherwise, it is taken to be the default      *              WSSecEngine instance (currently defined in the WSHandler      *              base class).      *      * TODO the WSHandler base class defines secEngine to be static, which      * is really bad, because the engine has mutable state on it.      */
specifier|private
name|WSSecurityEngine
name|getSecurityEngine
parameter_list|()
block|{
if|if
condition|(
name|secEngineOverride
operator|!=
literal|null
condition|)
block|{
return|return
name|secEngineOverride
return|;
block|}
return|return
name|secEngine
return|;
block|}
comment|/**      * @return      a freshly minted WSSecurityEngine instance, using the      *              (non-null) processor map, to be used to initialize the      *              WSSecurityEngine instance.      *      * TODO The WSS4J APIs leave something to be desired here, but hopefully      * we'll clean all this up in WSS4J-2.0      */
specifier|private
name|WSSecurityEngine
name|createSecurityEngine
parameter_list|(
specifier|final
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
assert|assert
name|map
operator|!=
literal|null
assert|;
specifier|final
name|WSSConfig
name|config
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|QName
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|val
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|String
condition|)
block|{
name|String
name|valStr
init|=
operator|(
operator|(
name|String
operator|)
name|val
operator|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"null"
operator|.
name|equals
argument_list|(
name|valStr
argument_list|)
operator|||
name|valStr
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|valStr
operator|=
literal|null
expr_stmt|;
block|}
name|config
operator|.
name|setProcessor
argument_list|(
name|key
argument_list|,
name|valStr
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|val
operator|instanceof
name|Processor
condition|)
block|{
name|config
operator|.
name|setProcessor
argument_list|(
name|key
argument_list|,
operator|(
name|Processor
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|config
operator|.
name|setProcessor
argument_list|(
name|key
argument_list|,
operator|(
name|String
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|WSSecurityEngine
name|ret
init|=
operator|new
name|WSSecurityEngine
argument_list|()
decl_stmt|;
name|ret
operator|.
name|setWssConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
comment|/**      * Create a SoapFault from a WSSecurityException, following the SOAP Message Security      * 1.1 specification, chapter 12 "Error Handling".      *       * When the Soap version is 1.1 then set the Fault/Code/Value from the fault code      * specified in the WSSecurityException (if it exists).      *       * Otherwise set the Fault/Code/Value to env:Sender and the Fault/Code/Subcode/Value      * as the fault code from the WSSecurityException.      */
specifier|private
name|SoapFault
name|createSoapFault
parameter_list|(
name|SoapVersion
name|version
parameter_list|,
name|WSSecurityException
name|e
parameter_list|)
block|{
name|SoapFault
name|fault
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
name|faultCode
init|=
name|e
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|==
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|,
name|version
operator|.
name|getSender
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|!=
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setSubCode
argument_list|(
name|faultCode
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fault
return|;
block|}
block|}
end_class

end_unit

