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
name|xkms
operator|.
name|itests
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|UUID
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
name|xkms
operator|.
name|handlers
operator|.
name|Applications
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
name|xkms
operator|.
name|handlers
operator|.
name|XKMSConstants
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
name|xkms
operator|.
name|itests
operator|.
name|BasicIntegrationTest
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
name|xkms
operator|.
name|model
operator|.
name|extensions
operator|.
name|ResultDetails
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|LocateRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|LocateResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|MessageAbstractType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|PrototypeKeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|QueryKeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RegisterRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RegisterResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMajorEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMinorEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|UnverifiedKeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|UseKeyWithType
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
name|xkms
operator|.
name|model
operator|.
name|xmldsig
operator|.
name|KeyInfoType
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
specifier|public
class|class
name|XKMSServiceTest
extends|extends
name|BasicIntegrationTest
block|{
specifier|private
specifier|static
specifier|final
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ObjectFactory
name|XKMS_OF
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ObjectFactory
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testLocatePKIX
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|Exception
block|{
name|LocateRequestType
name|request
init|=
name|XKMS_OF
operator|.
name|createLocateRequestType
argument_list|()
decl_stmt|;
name|setGenericRequestParams
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|QueryKeyBindingType
name|queryKeyBindingType
init|=
name|XKMS_OF
operator|.
name|createQueryKeyBindingType
argument_list|()
decl_stmt|;
name|UseKeyWithType
name|useKeyWithType
init|=
name|XKMS_OF
operator|.
name|createUseKeyWithType
argument_list|()
decl_stmt|;
name|useKeyWithType
operator|.
name|setIdentifier
argument_list|(
literal|"CN=Dave, OU=Apache, O=CXF, L=CGN, ST=NRW, C=DE"
argument_list|)
expr_stmt|;
name|useKeyWithType
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|PKIX
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|locateCertificate
argument_list|(
name|request
argument_list|,
name|queryKeyBindingType
argument_list|,
name|useKeyWithType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocateByEndpoint
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|Exception
block|{
name|LocateRequestType
name|request
init|=
name|XKMS_OF
operator|.
name|createLocateRequestType
argument_list|()
decl_stmt|;
name|setGenericRequestParams
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|QueryKeyBindingType
name|queryKeyBindingType
init|=
name|XKMS_OF
operator|.
name|createQueryKeyBindingType
argument_list|()
decl_stmt|;
name|UseKeyWithType
name|useKeyWithType
init|=
name|XKMS_OF
operator|.
name|createUseKeyWithType
argument_list|()
decl_stmt|;
name|useKeyWithType
operator|.
name|setIdentifier
argument_list|(
literal|"http://localhost:8080/services/TestService"
argument_list|)
expr_stmt|;
name|useKeyWithType
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|SERVICE_ENDPOINT
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|locateCertificate
argument_list|(
name|request
argument_list|,
name|queryKeyBindingType
argument_list|,
name|useKeyWithType
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|locateCertificate
parameter_list|(
name|LocateRequestType
name|request
parameter_list|,
name|QueryKeyBindingType
name|queryKeyBindingType
parameter_list|,
name|UseKeyWithType
name|useKeyWithType
parameter_list|)
block|{
name|queryKeyBindingType
operator|.
name|getUseKeyWith
argument_list|()
operator|.
name|add
argument_list|(
name|useKeyWithType
argument_list|)
expr_stmt|;
name|request
operator|.
name|setQueryKeyBinding
argument_list|(
name|queryKeyBindingType
argument_list|)
expr_stmt|;
name|LocateResultType
name|result
init|=
name|xkmsService
operator|.
name|locate
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|UnverifiedKeyBindingType
argument_list|>
name|keyBinding
init|=
name|result
operator|.
name|getUnverifiedKeyBinding
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keyBinding
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|KeyInfoType
name|keyInfo
init|=
name|keyBinding
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|keyInfo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setGenericRequestParams
parameter_list|(
name|MessageAbstractType
name|request
parameter_list|)
block|{
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|request
operator|.
name|setId
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyRegister
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|Exception
block|{
name|RegisterRequestType
name|request
init|=
operator|new
name|RegisterRequestType
argument_list|()
decl_stmt|;
name|setGenericRequestParams
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|RegisterResultType
name|result
init|=
name|xkmsService
operator|.
name|register
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SENDER
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_FAILURE
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMinor
argument_list|()
argument_list|)
expr_stmt|;
name|ResultDetails
name|message
init|=
operator|(
name|ResultDetails
operator|)
name|result
operator|.
name|getMessageExtension
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org.apache.cxf.xkms.model.xkms.PrototypeKeyBindingType must be set"
argument_list|,
name|message
operator|.
name|getDetails
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterWithoutKey
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|Exception
block|{
name|RegisterRequestType
name|request
init|=
operator|new
name|RegisterRequestType
argument_list|()
decl_stmt|;
name|setGenericRequestParams
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|PrototypeKeyBindingType
name|binding
init|=
operator|new
name|PrototypeKeyBindingType
argument_list|()
decl_stmt|;
name|KeyInfoType
name|keyInfo
init|=
operator|new
name|KeyInfoType
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setKeyInfo
argument_list|(
name|keyInfo
argument_list|)
expr_stmt|;
name|request
operator|.
name|setPrototypeKeyBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|RegisterResultType
name|result
init|=
name|xkmsService
operator|.
name|register
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SENDER
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_FAILURE
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

