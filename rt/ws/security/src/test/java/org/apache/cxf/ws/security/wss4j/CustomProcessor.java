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
name|WSDocInfo
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
name|message
operator|.
name|token
operator|.
name|SecurityContextToken
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

begin_comment
comment|/**  * a custom processor that inserts itself into the results vector  */
end_comment

begin_class
specifier|public
class|class
name|CustomProcessor
implements|implements
name|Processor
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|final
name|void
name|handleToken
parameter_list|(
specifier|final
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
name|elem
parameter_list|,
specifier|final
name|Crypto
name|crypto
parameter_list|,
specifier|final
name|Crypto
name|decCrypto
parameter_list|,
specifier|final
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
name|cb
parameter_list|,
specifier|final
name|WSDocInfo
name|wsDocInfo
parameter_list|,
specifier|final
name|java
operator|.
name|util
operator|.
name|Vector
name|returnResults
parameter_list|,
specifier|final
name|WSSConfig
name|config
parameter_list|)
throws|throws
name|WSSecurityException
block|{
specifier|final
name|java
operator|.
name|util
operator|.
name|Map
name|result
init|=
operator|new
name|WSSecurityEngineResult
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|,
operator|(
name|SecurityContextToken
operator|)
literal|null
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|returnResults
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getId
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

