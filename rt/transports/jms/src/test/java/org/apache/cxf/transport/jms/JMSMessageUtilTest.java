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
name|transport
operator|.
name|jms
package|;
end_package

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
name|UnsupportedEncodingException
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
name|JMSMessageUtilTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetEncoding
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|"Get the wrong encoding"
argument_list|,
name|JMSMessageUtils
operator|.
name|getEncoding
argument_list|(
literal|"text/xml; charset=utf-8"
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong encoding"
argument_list|,
name|JMSMessageUtils
operator|.
name|getEncoding
argument_list|(
literal|"text/xml"
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong encoding"
argument_list|,
name|JMSMessageUtils
operator|.
name|getEncoding
argument_list|(
literal|"text/xml; charset=GBK"
argument_list|)
argument_list|,
literal|"GBK"
argument_list|)
expr_stmt|;
try|try
block|{
name|JMSMessageUtils
operator|.
name|getEncoding
argument_list|(
literal|"text/xml; charset=asci"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect the exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"we should get the UnsupportedEncodingException here"
argument_list|,
name|ex
operator|instanceof
name|UnsupportedEncodingException
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

