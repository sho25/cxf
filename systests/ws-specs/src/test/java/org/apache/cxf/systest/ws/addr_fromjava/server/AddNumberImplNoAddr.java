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
name|systest
operator|.
name|ws
operator|.
name|addr_fromjava
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
name|WebMethod
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

begin_class
annotation|@
name|WebService
specifier|public
class|class
name|AddNumberImplNoAddr
block|{
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
name|AddNumbersException
block|{
return|return
name|execute
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
return|;
block|}
specifier|public
name|int
name|addNumbers2
parameter_list|(
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
block|{
return|return
name|number1
operator|+
name|number2
return|;
block|}
annotation|@
name|WebMethod
argument_list|(
name|action
operator|=
literal|"http://cxf.apache.org/input3"
argument_list|)
specifier|public
name|int
name|addNumbers3
parameter_list|(
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
throws|throws
name|AddNumbersException
block|{
return|return
name|execute
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
return|;
block|}
annotation|@
name|WebMethod
argument_list|(
name|action
operator|=
literal|"http://cxf.apache.org/input4"
argument_list|)
specifier|public
name|int
name|addNumbers4
parameter_list|(
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
throws|throws
name|AddNumbersException
block|{
return|return
name|number1
operator|+
name|number2
return|;
block|}
name|int
name|execute
parameter_list|(
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
throws|throws
name|AddNumbersException
block|{
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
throw|throw
operator|new
name|AddNumbersException
argument_list|(
literal|"Negative numbers can't be added!"
argument_list|,
literal|"Numbers: "
operator|+
name|number1
operator|+
literal|", "
operator|+
name|number2
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

