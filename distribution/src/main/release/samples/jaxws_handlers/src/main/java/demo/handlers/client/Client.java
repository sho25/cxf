begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|handlers
operator|.
name|client
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|handler
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|AddNumbers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|AddNumbersFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|AddNumbersService
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|handlers
operator|.
name|common
operator|.
name|SmallNumberHandler
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|static
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/handlers"
argument_list|,
literal|"AddNumbersService"
argument_list|)
decl_stmt|;
specifier|static
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/handlers"
argument_list|,
literal|"AddNumbersPort"
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
index|[]
name|args
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
name|err
operator|.
name|println
argument_list|(
literal|"please provide wsdl"
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
name|File
name|wsdl
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
name|AddNumbersService
name|service
init|=
operator|new
name|AddNumbersService
argument_list|(
name|wsdl
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|AddNumbers
name|port
init|=
operator|(
name|AddNumbers
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//Add client side handlers programmatically
name|SmallNumberHandler
name|sh
init|=
operator|new
name|SmallNumberHandler
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|newHandlerChain
init|=
operator|new
name|ArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|()
decl_stmt|;
name|newHandlerChain
operator|.
name|add
argument_list|(
name|sh
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getBinding
argument_list|()
operator|.
name|setHandlerChain
argument_list|(
name|newHandlerChain
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|number1
init|=
literal|10
decl_stmt|;
name|int
name|number2
init|=
literal|20
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"Invoking addNumbers(%d, %d)\n"
argument_list|,
name|number1
argument_list|,
name|number2
argument_list|)
expr_stmt|;
name|int
name|result
init|=
name|port
operator|.
name|addNumbers
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"The result of adding %d and %d is %d.\n\n"
argument_list|,
name|number1
argument_list|,
name|number2
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|number1
operator|=
literal|3
expr_stmt|;
name|number2
operator|=
literal|5
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"Invoking addNumbers(%d, %d)\n"
argument_list|,
name|number1
argument_list|,
name|number2
argument_list|)
expr_stmt|;
name|result
operator|=
name|port
operator|.
name|addNumbers
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"The result of adding %d and %d is %d.\n\n"
argument_list|,
name|number1
argument_list|,
name|number2
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|number1
operator|=
operator|-
literal|10
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"Invoking addNumbers(%d, %d)\n"
argument_list|,
name|number1
argument_list|,
name|number2
argument_list|)
expr_stmt|;
name|result
operator|=
name|port
operator|.
name|addNumbers
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"The result of adding %d and %d is %d.\n"
argument_list|,
name|number1
argument_list|,
name|number2
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AddNumbersFault
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"Caught AddNumbersFault: %s\n"
argument_list|,
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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

