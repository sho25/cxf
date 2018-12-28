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
name|tools
operator|.
name|misc
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ProcessorTestBase
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
name|tools
operator|.
name|misc
operator|.
name|XSDToWSDL
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|XSDToWSDLProcessorTest
extends|extends
name|ProcessorTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNewTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-t"
block|,
literal|"http://org.apache/invoice"
block|,
literal|"-n"
block|,
literal|"Invoice"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
literal|"-o"
block|,
literal|"Invoice_xsd.wsdl"
block|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/Invoice.xsd"
argument_list|)
block|}
decl_stmt|;
name|XSDToWSDL
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"Invoice_xsd.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New wsdl file is not generated: "
operator|+
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|outputFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileReader
name|fileReader
init|=
operator|new
name|FileReader
argument_list|(
name|outputFile
argument_list|)
decl_stmt|;
name|char
index|[]
name|chars
init|=
operator|new
name|char
index|[
literal|100
index|]
decl_stmt|;
name|int
name|size
init|=
literal|0
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|size
operator|<
name|outputFile
operator|.
name|length
argument_list|()
condition|)
block|{
name|int
name|readLen
init|=
name|fileReader
operator|.
name|read
argument_list|(
name|chars
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|chars
argument_list|,
literal|0
argument_list|,
name|readLen
argument_list|)
expr_stmt|;
name|size
operator|=
name|size
operator|+
name|readLen
expr_stmt|;
block|}
name|fileReader
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|serviceString
init|=
operator|new
name|String
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|serviceString
operator|.
name|indexOf
argument_list|(
literal|"<wsdl:types>"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceString
operator|.
name|indexOf
argument_list|(
literal|"<schema targetNamespace=\"http:/"
operator|+
literal|"/apache.org/Invoice\" xmlns=\"http:/"
operator|+
literal|"/www.w3.org/2001/XMLSchema\" xmlns:soap=\"http:/"
operator|+
literal|"/schemas.xmlsoap.org/wsdl/soap/\" xmlns:tns=\"http:/"
operator|+
literal|"/apache.org/Invoice\" xmlns:wsdl=\"http:/"
operator|+
literal|"/schemas.xmlsoap.org/wsdl/\">"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceString
operator|.
name|indexOf
argument_list|(
literal|"<complexType name=\"InvoiceHeader\">"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-t"
block|,
literal|"http://org.apache/invoice"
block|,
literal|"-n"
block|,
literal|"Invoice"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/Invoice.xsd"
argument_list|)
block|}
decl_stmt|;
name|XSDToWSDL
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"Invoice.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"PortType file is not generated"
argument_list|,
name|outputFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileReader
name|fileReader
init|=
operator|new
name|FileReader
argument_list|(
name|outputFile
argument_list|)
decl_stmt|;
name|char
index|[]
name|chars
init|=
operator|new
name|char
index|[
literal|100
index|]
decl_stmt|;
name|int
name|size
init|=
literal|0
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|size
operator|<
name|outputFile
operator|.
name|length
argument_list|()
condition|)
block|{
name|int
name|readLen
init|=
name|fileReader
operator|.
name|read
argument_list|(
name|chars
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|chars
argument_list|,
literal|0
argument_list|,
name|readLen
argument_list|)
expr_stmt|;
name|size
operator|=
name|size
operator|+
name|readLen
expr_stmt|;
block|}
name|fileReader
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|serviceString
init|=
operator|new
name|String
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|serviceString
operator|.
name|indexOf
argument_list|(
literal|"<wsdl:types>"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceString
operator|.
name|indexOf
argument_list|(
literal|"<schema targetNamespace=\"http:/"
operator|+
literal|"/apache.org/Invoice\" xmlns=\"http:/"
operator|+
literal|"/www.w3.org/2001/XMLSchema\" xmlns:soap=\"http:/"
operator|+
literal|"/schemas.xmlsoap.org/wsdl/soap/\" xmlns:tns=\"http:/"
operator|+
literal|"/apache.org/Invoice\" xmlns:wsdl=\"http:/"
operator|+
literal|"/schemas.xmlsoap.org/wsdl/\">"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceString
operator|.
name|indexOf
argument_list|(
literal|"<complexType name=\"InvoiceHeader\">"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getLocation
parameter_list|(
name|String
name|wsdlFile
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|File
argument_list|(
name|XSDToWSDLProcessorTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|wsdlFile
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

