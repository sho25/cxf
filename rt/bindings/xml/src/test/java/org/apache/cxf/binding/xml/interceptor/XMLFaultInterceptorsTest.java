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
name|binding
operator|.
name|xml
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|binding
operator|.
name|xml
operator|.
name|XMLFault
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
name|AbstractInDatabindingInterceptor
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
name|ClientFaultConverter
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
name|hello_world_xml_http
operator|.
name|bare
operator|.
name|types
operator|.
name|MyComplexStructType
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
name|XMLFaultInterceptorsTest
extends|extends
name|TestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRuntimeExceptionOfImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ns
init|=
literal|"http://apache.org/hello_world_xml_http/wrapped"
decl_stmt|;
name|common
argument_list|(
literal|"/wsdl/hello_world_xml_wrapped.wsdl"
argument_list|,
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"XMLPort"
argument_list|)
argument_list|,
name|MyComplexStructType
operator|.
name|class
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|xmlMessage
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|xmlMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
argument_list|)
expr_stmt|;
name|xmlMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"dummy exception"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|XMLFaultOutInterceptor
name|xfo
init|=
operator|new
name|XMLFaultOutInterceptor
argument_list|(
literal|"phase1"
argument_list|)
decl_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|xfo
argument_list|)
expr_stmt|;
name|InHelpInterceptor
name|ih
init|=
operator|new
name|InHelpInterceptor
argument_list|(
literal|"phase2"
argument_list|)
decl_stmt|;
name|ClientFaultConverter
name|cfc
init|=
operator|new
name|ClientFaultConverter
argument_list|(
literal|"phase3"
argument_list|)
decl_stmt|;
name|XMLFaultInInterceptor
name|xfi
init|=
operator|new
name|XMLFaultInInterceptor
argument_list|(
literal|"phase3"
argument_list|)
decl_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|ih
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|cfc
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|xfi
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doIntercept
argument_list|(
name|xmlMessage
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|xmlMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xmlMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|instanceof
name|XMLFault
argument_list|)
expr_stmt|;
name|XMLFault
name|xfault
init|=
operator|(
name|XMLFault
operator|)
name|xmlMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check message expected - dummy exception"
argument_list|,
name|xfault
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"dummy exception"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|InHelpInterceptor
extends|extends
name|AbstractInDatabindingInterceptor
block|{
name|InHelpInterceptor
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
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|(
name|ByteArrayOutputStream
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|bis
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|xsr
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bis
argument_list|)
decl_stmt|;
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|xsr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

