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
name|corba
operator|.
name|common
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
name|PrintStream
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
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|ToolTestBase
extends|extends
name|Assert
block|{
specifier|protected
name|PrintStream
name|oldStdErr
decl_stmt|;
specifier|protected
name|PrintStream
name|oldStdOut
decl_stmt|;
specifier|protected
name|URL
name|wsdlLocation
decl_stmt|;
specifier|protected
name|URL
name|idlLocation
decl_stmt|;
specifier|protected
name|ByteArrayOutputStream
name|errOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|protected
name|ByteArrayOutputStream
name|stdOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|oldStdErr
operator|=
name|System
operator|.
name|err
expr_stmt|;
name|oldStdOut
operator|=
name|System
operator|.
name|out
expr_stmt|;
name|System
operator|.
name|setErr
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|errOut
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|stdOut
argument_list|)
argument_list|)
expr_stmt|;
name|wsdlLocation
operator|=
name|ToolTestBase
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
expr_stmt|;
name|idlLocation
operator|=
name|ToolTestBase
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/idl/HelloWorld.idl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|System
operator|.
name|setErr
argument_list|(
name|oldStdErr
argument_list|)
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|oldStdOut
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getStdOut
parameter_list|()
block|{
return|return
operator|new
name|String
argument_list|(
name|stdOut
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getStdErr
parameter_list|()
block|{
return|return
operator|new
name|String
argument_list|(
name|errOut
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

