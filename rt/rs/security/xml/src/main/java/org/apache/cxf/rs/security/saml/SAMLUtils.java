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
name|rs
operator|.
name|security
operator|.
name|saml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|CallbackHandler
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|CryptoLoader
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|SecurityUtils
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
name|saml
operator|.
name|ext
operator|.
name|AssertionWrapper
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
name|saml
operator|.
name|ext
operator|.
name|SAMLParms
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SAMLUtils
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
name|SAMLUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SAMLUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|AssertionWrapper
name|createAssertion
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|CallbackHandler
name|handler
init|=
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|SAMLUtils
operator|.
name|class
argument_list|,
name|SecurityConstants
operator|.
name|SAML_CALLBACK_HANDLER
argument_list|)
decl_stmt|;
name|SAMLParms
name|samlParms
init|=
operator|new
name|SAMLParms
argument_list|()
decl_stmt|;
name|samlParms
operator|.
name|setCallbackHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
try|try
block|{
name|AssertionWrapper
name|assertion
init|=
operator|new
name|AssertionWrapper
argument_list|(
name|samlParms
argument_list|)
decl_stmt|;
name|boolean
name|selfSignAssertion
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SELF_SIGN_SAML_ASSERTION
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|selfSignAssertion
condition|)
block|{
comment|//--- This code will be moved to a common utility class
name|Crypto
name|crypto
init|=
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|SecurityUtils
operator|.
name|getUserName
argument_list|(
name|message
argument_list|,
name|crypto
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
name|assertion
return|;
block|}
name|String
name|password
init|=
name|SecurityUtils
operator|.
name|getPassword
argument_list|(
name|message
argument_list|,
name|user
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|,
name|SAMLUtils
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|signAssertion
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|crypto
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|assertion
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|", stacktrace: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

