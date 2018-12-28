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
name|management
operator|.
name|utils
package|;
end_package

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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertFalse
import|;
end_import

begin_class
specifier|public
class|class
name|ManagementConsoleTest
extends|extends
name|Assert
block|{
specifier|private
name|ManagementConsole
name|mc
init|=
operator|new
name|ManagementConsole
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|paraserCommandTest
parameter_list|()
block|{
name|String
index|[]
name|listArgs
init|=
operator|new
name|String
index|[]
block|{
literal|"--operation"
block|,
literal|"list"
block|}
decl_stmt|;
name|mc
operator|.
name|parserArguments
argument_list|(
name|listArgs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"It is not right operation name"
argument_list|,
literal|"list"
argument_list|,
name|mc
operator|.
name|operationName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The portName should be cleared"
argument_list|,
literal|""
argument_list|,
name|mc
operator|.
name|portName
argument_list|)
expr_stmt|;
name|String
index|[]
name|startArgs
init|=
operator|new
name|String
index|[]
block|{
literal|"-o"
block|,
literal|"start"
block|,
literal|"--jmx"
block|,
literal|"service:jmx:rmi:///jndi/rmi://localhost:1234/jmxrmi"
block|,
literal|"--service"
block|,
literal|"\"{http://apache.org/hello_world_soap_http}SOAPService\""
block|,
literal|"--port"
block|,
literal|"\"{http://apache.org/hello_world_soap_http}SoapPort\""
block|}
decl_stmt|;
name|mc
operator|.
name|parserArguments
argument_list|(
name|startArgs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"It is not right operation name"
argument_list|,
literal|"start"
argument_list|,
name|mc
operator|.
name|operationName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"It is not right port name"
argument_list|,
literal|"\"{http://apache.org/hello_world_soap_http}SoapPort\""
argument_list|,
name|mc
operator|.
name|portName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"It is not right service name"
argument_list|,
literal|"\"{http://apache.org/hello_world_soap_http}SOAPService\""
argument_list|,
name|mc
operator|.
name|serviceName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"It is not a jmx url"
argument_list|,
literal|"service:jmx:rmi:///jndi/rmi://localhost:1234/jmxrmi"
argument_list|,
name|mc
operator|.
name|jmxServerURL
argument_list|)
expr_stmt|;
name|String
index|[]
name|errorArgs
init|=
operator|new
name|String
index|[]
block|{
literal|"--op"
block|,
literal|"listAll"
block|}
decl_stmt|;
name|assertFalse
argument_list|(
literal|"the arguments are wrong"
argument_list|,
name|mc
operator|.
name|parserArguments
argument_list|(
name|errorArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

