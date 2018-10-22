begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|mtom
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

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
name|File
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
name|net
operator|.
name|URI
import|;
end_import

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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|ws
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|mime
operator|.
name|TestMtomPortType
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
name|mime
operator|.
name|TestMtomService
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mime"
argument_list|,
literal|"TestMtomService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mime"
argument_list|,
literal|"TestMtomPort"
argument_list|)
decl_stmt|;
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Please specify the WSDL file."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|URL
name|wsdlURL
decl_stmt|;
name|File
name|wsdlFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wsdlURL
operator|=
name|wsdlFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wsdlURL
operator|=
operator|new
name|URL
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
name|TestMtomService
name|tms
init|=
operator|new
name|TestMtomService
argument_list|(
name|wsdlURL
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|TestMtomPortType
name|port
init|=
operator|(
name|TestMtomPortType
operator|)
name|tms
operator|.
name|getPort
argument_list|(
name|PORT_NAME
argument_list|,
name|TestMtomPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getBinding
argument_list|()
decl_stmt|;
operator|(
operator|(
name|SOAPBinding
operator|)
name|binding
operator|)
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|URL
name|fileURL
init|=
name|Client
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/me.bmp"
argument_list|)
decl_stmt|;
name|File
name|aFile
init|=
operator|new
name|File
argument_list|(
operator|new
name|URI
argument_list|(
name|fileURL
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|fileSize
init|=
name|aFile
operator|.
name|length
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Filesize of me.bmp image is: "
operator|+
name|fileSize
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nStarting MTOM Test using basic byte array:"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|new
name|Holder
argument_list|<>
argument_list|(
literal|"Sam"
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|param
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|param
operator|.
name|value
operator|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|fileSize
index|]
expr_stmt|;
name|InputStream
name|in
init|=
name|fileURL
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|in
operator|.
name|read
argument_list|(
name|param
operator|.
name|value
argument_list|)
decl_stmt|;
while|while
condition|(
name|len
operator|<
name|fileSize
condition|)
block|{
name|len
operator|+=
name|in
operator|.
name|read
argument_list|(
name|param
operator|.
name|value
argument_list|,
name|len
argument_list|,
call|(
name|int
call|)
argument_list|(
name|fileSize
operator|-
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Sending the me.bmp image to server"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Sending a name value of "
operator|+
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|port
operator|.
name|testByteArray
argument_list|(
name|name
argument_list|,
name|param
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Received byte[] back from server, returned size is "
operator|+
name|param
operator|.
name|value
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Returned string value is "
operator|+
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|Image
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|param
operator|.
name|value
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Loaded image from byte[] successfully, hashCode="
operator|+
name|image
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Successfully ran MTOM/byte array demo"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nStarting MTOM test with DataHandler:"
argument_list|)
expr_stmt|;
name|name
operator|.
name|value
operator|=
literal|"Bob"
expr_stmt|;
name|Holder
argument_list|<
name|DataHandler
argument_list|>
name|handler
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|handler
operator|.
name|value
operator|=
operator|new
name|DataHandler
argument_list|(
name|fileURL
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Sending the me.bmp image to server"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Sending a name value of "
operator|+
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|port
operator|.
name|testDataHandler
argument_list|(
name|name
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|InputStream
name|mtomIn
init|=
name|handler
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|fileSize
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|mtomIn
operator|.
name|read
argument_list|()
init|;
name|i
operator|!=
operator|-
literal|1
condition|;
name|i
operator|=
name|mtomIn
operator|.
name|read
argument_list|()
control|)
block|{
name|fileSize
operator|++
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Received DataHandler back from server, "
operator|+
literal|"returned size is "
operator|+
name|fileSize
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--Returned string value is "
operator|+
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Successfully ran MTOM/DataHandler demo"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

