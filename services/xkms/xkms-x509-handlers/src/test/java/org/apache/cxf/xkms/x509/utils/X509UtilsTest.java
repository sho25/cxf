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
name|x509
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|exception
operator|.
name|XKMSRequestException
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
name|ValidateRequestType
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
name|x509
operator|.
name|validator
operator|.
name|BasicValidationTest
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
name|x509
operator|.
name|validator
operator|.
name|ValidateRequestParser
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

begin_class
specifier|public
class|class
name|X509UtilsTest
extends|extends
name|BasicValidationTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CERT_DN
init|=
literal|"CN=www.anothersts.com, L=CGN, ST=NRW, C=DE, O=AnotherSTS"
decl_stmt|;
annotation|@
name|Test
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
specifier|public
name|void
name|extractValidatingCertsOK
parameter_list|()
throws|throws
name|JAXBException
block|{
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/validateRequestOK.xml"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|JAXBElement
argument_list|<
name|ValidateRequestType
argument_list|>
name|request
init|=
operator|(
name|JAXBElement
argument_list|<
name|ValidateRequestType
argument_list|>
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certs
init|=
name|ValidateRequestParser
operator|.
name|parse
argument_list|(
name|request
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Exactly one certificate should be found"
argument_list|,
literal|1
argument_list|,
name|certs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Unexcpected certificate DN"
argument_list|,
name|CERT_DN
argument_list|,
name|certs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSubjectDN
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|XKMSRequestException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|extractValidatingCertsCorrupted
parameter_list|()
throws|throws
name|JAXBException
block|{
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/validateRequestCorrupted.xml"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|JAXBElement
argument_list|<
name|ValidateRequestType
argument_list|>
name|request
init|=
operator|(
name|JAXBElement
argument_list|<
name|ValidateRequestType
argument_list|>
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|ValidateRequestParser
operator|.
name|parse
argument_list|(
name|request
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

