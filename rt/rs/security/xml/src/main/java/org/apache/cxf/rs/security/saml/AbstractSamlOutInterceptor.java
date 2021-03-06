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
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Base64Exception
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
name|Base64Utility
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
name|AbstractPhaseInterceptor
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
name|WSProviderConfig
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
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSamlOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
static|static
block|{
name|WSProviderConfig
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|useDeflateEncoding
init|=
literal|true
decl_stmt|;
specifier|protected
name|AbstractSamlOutInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUseDeflateEncoding
parameter_list|(
name|boolean
name|deflate
parameter_list|)
block|{
name|useDeflateEncoding
operator|=
name|deflate
expr_stmt|;
block|}
specifier|protected
name|SamlAssertionWrapper
name|createAssertion
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
return|return
name|SAMLUtils
operator|.
name|createAssertion
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|protected
name|String
name|encodeToken
parameter_list|(
name|String
name|assertion
parameter_list|)
throws|throws
name|Base64Exception
block|{
name|byte
index|[]
name|tokenBytes
init|=
name|assertion
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
if|if
condition|(
name|useDeflateEncoding
condition|)
block|{
name|tokenBytes
operator|=
operator|new
name|DeflateEncoderDecoder
argument_list|()
operator|.
name|deflateToken
argument_list|(
name|tokenBytes
argument_list|)
expr_stmt|;
block|}
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Base64Utility
operator|.
name|encode
argument_list|(
name|tokenBytes
argument_list|,
literal|0
argument_list|,
name|tokenBytes
operator|.
name|length
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

