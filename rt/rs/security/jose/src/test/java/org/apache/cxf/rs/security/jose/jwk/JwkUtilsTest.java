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
name|jose
operator|.
name|jwk
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JwkUtilsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSA_KEY
init|=
literal|"{"
operator|+
literal|"\"kty\": \"RSA\","
operator|+
literal|"\"n\": \"0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx4cbbfAAt"
operator|+
literal|"VT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMstn6"
operator|+
literal|"4tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FD"
operator|+
literal|"W2QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n9"
operator|+
literal|"1CbOpbISD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINH"
operator|+
literal|"aQ-G_xBniIqbw0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw\","
operator|+
literal|"\"e\": \"AQAB\","
operator|+
literal|"\"alg\": \"RS256\","
operator|+
literal|"\"kid\": \"2011-04-29\""
operator|+
literal|"}\""
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testRsaKeyThumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|RSA_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"NzbLsXh8uDCcd-6MNwXF4W_7noWXFZAfHkxZsRGC9Xs"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

