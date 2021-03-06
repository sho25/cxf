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
name|policy
operator|.
name|interceptors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|AbstractPolicyInterceptorProvider
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
name|SP11Constants
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
name|SP12Constants
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WSSecurityPolicyInterceptorProvider
extends|extends
name|AbstractPolicyInterceptorProvider
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2092269997296804632L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|QName
argument_list|>
name|ASSERTION_TYPES
decl_stmt|;
static|static
block|{
name|ASSERTION_TYPES
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|LAYOUT
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ALGORITHM_SUITE
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPT_SIGNATURE
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|PROTECT_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPT_BEFORE_SIGNING
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGN_BEFORE_ENCRYPTING
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|WSS10
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|WSS11
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|TRUST_13
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|PROTECTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|X509_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNATURE_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|TRANSPORT_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|INITIATOR_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|INITIATOR_SIGNATURE_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|INITIATOR_ENCRYPTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|RECIPIENT_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|RECIPIENT_SIGNATURE_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|RECIPIENT_ENCRYPTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|REQUIRED_PARTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|REQUIRED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|CONTENT_ENCRYPTED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|LAYOUT
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ALGORITHM_SUITE
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ENCRYPT_SIGNATURE
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|PROTECT_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ENCRYPT_BEFORE_SIGNING
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SIGN_BEFORE_ENCRYPTING
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|WSS10
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|WSS11
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|TRUST_10
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|PROTECTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|X509_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ENCRYPTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SIGNATURE_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|TRANSPORT_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|INITIATOR_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|INITIATOR_SIGNATURE_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|INITIATOR_ENCRYPTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|RECIPIENT_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|RECIPIENT_SIGNATURE_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|RECIPIENT_ENCRYPTION_TOKEN
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|REQUIRED_PARTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|REQUIRED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SIGNED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|CONTENT_ENCRYPTED_ELEMENTS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP11Constants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSSecurityPolicyInterceptorProvider
parameter_list|()
block|{
name|super
argument_list|(
name|ASSERTION_TYPES
argument_list|)
expr_stmt|;
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|SecurityVerificationOutInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

