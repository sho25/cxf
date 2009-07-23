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
name|server
package|;
end_package

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
name|IOException
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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"TestMtomService"
argument_list|,
name|portName
operator|=
literal|"TestMtomPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.mime.TestMtomPortType"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/mime"
argument_list|)
specifier|public
class|class
name|TestMtomPortTypeImpl
implements|implements
name|TestMtomPortType
block|{
specifier|public
name|void
name|testByteArray
parameter_list|(
name|Holder
argument_list|<
name|String
argument_list|>
name|name
parameter_list|,
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|attachinfo
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received image from client"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The image data size is "
operator|+
name|attachinfo
operator|.
name|value
operator|.
name|length
argument_list|)
expr_stmt|;
name|name
operator|.
name|value
operator|=
literal|"Hello "
operator|+
name|name
operator|.
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|testDataHandler
parameter_list|(
name|Holder
argument_list|<
name|String
argument_list|>
name|name
parameter_list|,
name|Holder
argument_list|<
name|DataHandler
argument_list|>
name|attachinfo
parameter_list|)
block|{
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received image with mtom enabled from client"
argument_list|)
expr_stmt|;
name|InputStream
name|mtomIn
init|=
name|attachinfo
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|copy
argument_list|(
name|mtomIn
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The image data size is "
operator|+
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|.
name|value
operator|=
literal|"Hello "
operator|+
name|name
operator|.
name|value
expr_stmt|;
name|mtomIn
operator|.
name|close
argument_list|()
expr_stmt|;
name|attachinfo
operator|.
name|value
operator|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|attachinfo
operator|.
name|value
operator|.
name|getContentType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|int
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|total
operator|+=
name|n
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
block|}
end_class

end_unit

