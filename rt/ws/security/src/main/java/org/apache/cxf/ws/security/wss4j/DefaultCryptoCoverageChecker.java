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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|Names
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
name|CryptoCoverageUtil
operator|.
name|CoverageScope
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
name|CryptoCoverageUtil
operator|.
name|CoverageType
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
comment|/**  * This utility extends the CryptoCoverageChecker to provide an easy way to check to see  * if the SOAP (1.1 + 1.2) Body was signed and/or encrypted, if the Timestamp was signed,  * if the WS-Addressing ReplyTo and FaultTo headers were signed, and if the UsernameToken  * was encrypted.  *  * The default configuration is that the SOAP Body, Timestamp must be signed, WS-Addressing  * ReplyTo and FaultTo headers must be signed, and a WSS UsernameToken must be encrypted  * (if they exist in the message payload).  */
end_comment

begin_class
specifier|public
class|class
name|DefaultCryptoCoverageChecker
extends|extends
name|CryptoCoverageChecker
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_NS
init|=
name|WSConstants
operator|.
name|URI_SOAP11_ENV
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP12_NS
init|=
name|WSConstants
operator|.
name|URI_SOAP12_ENV
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSU_NS
init|=
name|WSConstants
operator|.
name|WSU_NS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSSE_NS
init|=
name|WSConstants
operator|.
name|WSSE_NS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NS
init|=
name|Names
operator|.
name|WSA_NAMESPACE_NAME
decl_stmt|;
specifier|private
name|boolean
name|signBody
decl_stmt|;
specifier|private
name|boolean
name|signTimestamp
decl_stmt|;
specifier|private
name|boolean
name|encryptBody
decl_stmt|;
specifier|private
name|boolean
name|signAddressingHeaders
decl_stmt|;
specifier|private
name|boolean
name|signUsernameToken
decl_stmt|;
specifier|private
name|boolean
name|encryptUsernameToken
decl_stmt|;
comment|/**      * Creates a new instance. Enforces that the SOAP Body, Timestamp, and WS-Addressing      * ReplyTo and FaultTo headers must be signed (if they exist in the message payload).      */
specifier|public
name|DefaultCryptoCoverageChecker
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|prefixMap
operator|.
name|put
argument_list|(
literal|"soapenv"
argument_list|,
name|SOAP_NS
argument_list|)
expr_stmt|;
name|prefixMap
operator|.
name|put
argument_list|(
literal|"soapenv12"
argument_list|,
name|SOAP12_NS
argument_list|)
expr_stmt|;
name|prefixMap
operator|.
name|put
argument_list|(
literal|"wsu"
argument_list|,
name|WSU_NS
argument_list|)
expr_stmt|;
name|prefixMap
operator|.
name|put
argument_list|(
literal|"wsse"
argument_list|,
name|WSSE_NS
argument_list|)
expr_stmt|;
name|prefixMap
operator|.
name|put
argument_list|(
literal|"wsa"
argument_list|,
name|WSA_NS
argument_list|)
expr_stmt|;
comment|// Sign SOAP Body
name|setSignBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Sign Timestamp
name|setSignTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Sign Addressing Headers
name|setSignAddressingHeaders
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Encrypt UsernameToken
name|setEncryptUsernameToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSignBody
parameter_list|()
block|{
return|return
name|signBody
return|;
block|}
specifier|public
specifier|final
name|void
name|setSignBody
parameter_list|(
name|boolean
name|signBody
parameter_list|)
block|{
name|this
operator|.
name|signBody
operator|=
name|signBody
expr_stmt|;
name|XPathExpression
name|soap11Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Body"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Body"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
if|if
condition|(
name|signBody
condition|)
block|{
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isSignTimestamp
parameter_list|()
block|{
return|return
name|signTimestamp
return|;
block|}
specifier|public
specifier|final
name|void
name|setSignTimestamp
parameter_list|(
name|boolean
name|signTimestamp
parameter_list|)
block|{
name|this
operator|.
name|signTimestamp
operator|=
name|signTimestamp
expr_stmt|;
name|XPathExpression
name|soap11Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Header/wsse:Security/wsu:Timestamp"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Header/wsse:Security/wsu:Timestamp"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
if|if
condition|(
name|signTimestamp
condition|)
block|{
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isEncryptBody
parameter_list|()
block|{
return|return
name|encryptBody
return|;
block|}
specifier|public
specifier|final
name|void
name|setEncryptBody
parameter_list|(
name|boolean
name|encryptBody
parameter_list|)
block|{
name|this
operator|.
name|encryptBody
operator|=
name|encryptBody
expr_stmt|;
name|XPathExpression
name|soap11Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Body"
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|,
name|CoverageScope
operator|.
name|CONTENT
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Body"
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|,
name|CoverageScope
operator|.
name|CONTENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptBody
condition|)
block|{
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isSignAddressingHeaders
parameter_list|()
block|{
return|return
name|signAddressingHeaders
return|;
block|}
specifier|public
specifier|final
name|void
name|setSignAddressingHeaders
parameter_list|(
name|boolean
name|signAddressingHeaders
parameter_list|)
block|{
name|this
operator|.
name|signAddressingHeaders
operator|=
name|signAddressingHeaders
expr_stmt|;
name|XPathExpression
name|soap11Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Header/wsa:ReplyTo"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap11Expression2
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Header/wsa:FaultTo"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Header/wsa:ReplyTo"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression2
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Header/wsa:FaultTo"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
if|if
condition|(
name|signAddressingHeaders
condition|)
block|{
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression2
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression2
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression2
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression2
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression2
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isEncryptUsernameToken
parameter_list|()
block|{
return|return
name|encryptUsernameToken
return|;
block|}
specifier|public
specifier|final
name|void
name|setEncryptUsernameToken
parameter_list|(
name|boolean
name|encryptUsernameToken
parameter_list|)
block|{
name|this
operator|.
name|encryptUsernameToken
operator|=
name|encryptUsernameToken
expr_stmt|;
name|XPathExpression
name|soap11Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Header/wsse:Security/wsse:UsernameToken"
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Header/wsse:Security/wsse:UsernameToken"
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptUsernameToken
condition|)
block|{
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isSignUsernameToken
parameter_list|()
block|{
return|return
name|signUsernameToken
return|;
block|}
specifier|public
name|void
name|setSignUsernameToken
parameter_list|(
name|boolean
name|signUsernameToken
parameter_list|)
block|{
name|this
operator|.
name|signUsernameToken
operator|=
name|signUsernameToken
expr_stmt|;
name|XPathExpression
name|soap11Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv:Envelope/soapenv:Header/wsse:Security/wsse:UsernameToken"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
name|XPathExpression
name|soap12Expression
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"/soapenv12:Envelope/soapenv12:Header/wsse:Security/wsse:UsernameToken"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
decl_stmt|;
if|if
condition|(
name|signUsernameToken
condition|)
block|{
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|add
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap11Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap11Expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPaths
operator|.
name|contains
argument_list|(
name|soap12Expression
argument_list|)
condition|)
block|{
name|xPaths
operator|.
name|remove
argument_list|(
name|soap12Expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

