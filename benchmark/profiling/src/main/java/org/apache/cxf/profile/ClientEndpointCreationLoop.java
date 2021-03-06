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
name|profile
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jprofiler
operator|.
name|api
operator|.
name|agent
operator|.
name|Controller
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
name|net
operator|.
name|URISyntaxException
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
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ClientEndpointCreationLoop
block|{
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
specifier|private
name|ClientEndpointCreationLoop
parameter_list|()
block|{     }
specifier|private
name|void
name|iteration
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param args      * @throws URISyntaxException      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|Controller
operator|.
name|stopAllocRecording
argument_list|()
expr_stmt|;
name|Controller
operator|.
name|stopCPURecording
argument_list|()
expr_stmt|;
name|ClientEndpointCreationLoop
name|ecl
init|=
operator|new
name|ClientEndpointCreationLoop
argument_list|()
decl_stmt|;
name|ecl
operator|.
name|iteration
argument_list|()
expr_stmt|;
comment|// warm up.
name|Controller
operator|.
name|startCPURecording
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Controller
operator|.
name|startAllocRecording
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|count
condition|;
name|x
operator|++
control|)
block|{
name|ecl
operator|.
name|iteration
argument_list|()
expr_stmt|;
block|}
name|Controller
operator|.
name|stopAllocRecording
argument_list|()
expr_stmt|;
name|Controller
operator|.
name|stopCPURecording
argument_list|()
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|Controller
operator|.
name|saveSnapshot
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

