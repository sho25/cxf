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
name|systest
operator|.
name|aegis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|test
operator|.
name|TestUtilities
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath:aegisWSDLNSBeans.xml"
block|}
argument_list|)
specifier|public
class|class
name|AegisWSDLNSTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|AegisWSDLNSTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AegisJaxWsWsdlNs
name|client
decl_stmt|;
specifier|public
name|AegisWSDLNSTest
parameter_list|()
block|{     }
specifier|private
name|void
name|setupForTest
parameter_list|(
name|boolean
name|specifyWsdl
parameter_list|)
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|AegisJaxWsWsdlNs
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|specifyWsdl
condition|)
block|{
name|factory
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://v1_1_2.rtf2pdf.doc.ws.daisy.marbes.cz"
argument_list|,
literal|"AegisJaxWsWsdlNsImplService"
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setWsdlLocation
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/aegisJaxWsWSDLNS?wsdl"
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/aegisJaxWsWSDLNS"
argument_list|)
expr_stmt|;
name|client
operator|=
operator|(
name|AegisJaxWsWsdlNs
operator|)
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AegisJaxWsWsdlNs
operator|.
name|VO
name|vo
init|=
operator|new
name|AegisJaxWsWsdlNs
operator|.
name|VO
argument_list|()
decl_stmt|;
name|vo
operator|.
name|setStr
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|client
operator|.
name|updateVO
argument_list|(
name|vo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithWsdl
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|AegisJaxWsWsdlNs
operator|.
name|VO
name|vo
init|=
operator|new
name|AegisJaxWsWsdlNs
operator|.
name|VO
argument_list|()
decl_stmt|;
name|vo
operator|.
name|setStr
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|client
operator|.
name|updateVO
argument_list|(
name|vo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratedWsdlNs
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/aegisJaxWsWSDLNS?wsdl"
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|TestUtilities
name|util
init|=
operator|new
name|TestUtilities
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|util
operator|.
name|addDefaultNamespaces
argument_list|()
expr_stmt|;
name|util
operator|.
name|assertValid
argument_list|(
literal|"//wsdl:definitions[@targetNamespace"
operator|+
literal|"='http://v1_1_2.rtf2pdf.doc.ws.daisy.marbes.cz']"
argument_list|,
name|dom
argument_list|)
expr_stmt|;
comment|//should be a targetNamespace for "http://wo.rtf2pdf.doc.ws.daisy.marbes.cz"
comment|//as VO type specified in the SEI
name|util
operator|.
name|assertValid
argument_list|(
literal|"//wsdl:definitions/wsdl:types/xsd:schema[@targetNamespace"
operator|+
literal|"='http://wo.rtf2pdf.doc.ws.daisy.marbes.cz']"
argument_list|,
name|dom
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUsingCorrectMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Integer
name|result
init|=
name|client
operator|.
name|updateInteger
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|20
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|result
operator|.
name|intValue
argument_list|()
argument_list|,
literal|20
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

