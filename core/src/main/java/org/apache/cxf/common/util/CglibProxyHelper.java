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
name|Method
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
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|Enhancer
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|MethodInterceptor
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|MethodProxy
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
class|class
name|CglibProxyHelper
extends|extends
name|ProxyHelper
block|{
name|CglibProxyHelper
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"net.sf.cglib.proxy.Enhancer"
argument_list|)
expr_stmt|;
name|Class
operator|.
name|forName
argument_list|(
literal|"net.sf.cglib.proxy.MethodInterceptor"
argument_list|)
expr_stmt|;
name|Class
operator|.
name|forName
argument_list|(
literal|"net.sf.cglib.proxy.MethodProxy"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getProxyInternal
parameter_list|(
name|ClassLoader
name|loader
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
parameter_list|,
specifier|final
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
name|h
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|superClass
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|theInterfaces
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|interfaces
control|)
block|{
if|if
condition|(
operator|!
name|c
operator|.
name|isInterface
argument_list|()
condition|)
block|{
if|if
condition|(
name|superClass
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only a single superclass is supported"
argument_list|)
throw|;
block|}
name|superClass
operator|=
name|c
expr_stmt|;
block|}
else|else
block|{
name|theInterfaces
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|superClass
operator|!=
literal|null
condition|)
block|{
name|Enhancer
name|enhancer
init|=
operator|new
name|Enhancer
argument_list|()
decl_stmt|;
name|enhancer
operator|.
name|setClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|enhancer
operator|.
name|setSuperclass
argument_list|(
name|superClass
argument_list|)
expr_stmt|;
name|enhancer
operator|.
name|setInterfaces
argument_list|(
name|theInterfaces
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|enhancer
operator|.
name|setCallback
argument_list|(
operator|new
name|MethodInterceptor
argument_list|()
block|{
specifier|public
name|Object
name|intercept
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|,
name|MethodProxy
name|proxy
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
name|h
operator|.
name|invoke
argument_list|(
name|obj
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|enhancer
operator|.
name|create
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getProxyInternal
argument_list|(
name|loader
argument_list|,
name|interfaces
argument_list|,
name|h
argument_list|)
return|;
block|}
block|}
end_class

end_unit

