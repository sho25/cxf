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
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|HandlerChain
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
name|types
operator|.
name|FaultDetail
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"AddNumbers"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/handlers"
argument_list|,
name|portName
operator|=
literal|"AddNumbersPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.handlers.AddNumbers"
argument_list|,
name|serviceName
operator|=
literal|"AddNumbersService"
argument_list|)
annotation|@
name|HandlerChain
argument_list|(
name|file
operator|=
literal|"/demo_handlers.xml"
argument_list|)
specifier|public
class|class
name|AddNumbersImpl
implements|implements
name|AddNumbers
block|{
comment|/**      * @param number1      * @param number2      * @return The sum      * @throws AddNumbersException      *             if any of the numbers to be added is negative.      */
specifier|public
name|int
name|addNumbers
parameter_list|(
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
throws|throws
name|AddNumbersFault
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"addNumbers called....."
operator|+
name|number1
operator|+
literal|":"
operator|+
name|number2
argument_list|)
expr_stmt|;
if|if
condition|(
name|number1
operator|<
literal|0
operator|||
name|number2
operator|<
literal|0
condition|)
block|{
name|String
name|message
init|=
literal|"Negative number cant be added!"
decl_stmt|;
name|String
name|detail
init|=
literal|"Numbers: "
operator|+
name|number1
operator|+
literal|", "
operator|+
name|number2
decl_stmt|;
name|FaultDetail
name|fault
init|=
operator|new
name|FaultDetail
argument_list|()
decl_stmt|;
name|fault
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setFaultInfo
argument_list|(
name|detail
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AddNumbersFault
argument_list|(
name|message
argument_list|,
name|fault
argument_list|)
throw|;
block|}
return|return
name|number1
operator|+
name|number2
return|;
block|}
block|}
end_class

end_unit

