begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|server
operator|.
name|socket
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|StreamingOutput
import|;
end_import

begin_class
specifier|public
class|class
name|SparkStreamingOutput
implements|implements
name|StreamingOutput
block|{
specifier|private
name|BufferedReader
name|sparkInputStream
decl_stmt|;
specifier|public
name|SparkStreamingOutput
parameter_list|(
name|BufferedReader
name|sparkInputStream
parameter_list|)
block|{
name|this
operator|.
name|sparkInputStream
operator|=
name|sparkInputStream
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|PrintStream
name|printStream
init|=
operator|new
name|PrintStream
argument_list|(
name|output
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|s
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|s
operator|=
name|sparkInputStream
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"<batchEnd>"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
break|break;
block|}
name|printStream
operator|.
name|println
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
