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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_class
specifier|public
class|class
name|ExtensionInvocationHandler
implements|implements
name|InvocationHandler
block|{
specifier|private
name|Object
name|obj
decl_stmt|;
specifier|public
name|ExtensionInvocationHandler
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|obj
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|obj
argument_list|,
name|args
argument_list|)
return|;
block|}
comment|//in case obj has the required method with exact signature despite its class
comment|//not being assignable from the class declaring the specified method
name|Method
name|m
init|=
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|invoke
argument_list|(
name|obj
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
end_class

end_unit

