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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|ProxyHelper
block|{
specifier|static
specifier|final
name|ProxyHelper
name|HELPER
decl_stmt|;
static|static
block|{
name|ProxyHelper
name|theHelper
init|=
literal|null
decl_stmt|;
try|try
block|{
name|theHelper
operator|=
operator|new
name|CglibProxyHelper
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|theHelper
operator|=
operator|new
name|ProxyHelper
argument_list|()
expr_stmt|;
block|}
name|HELPER
operator|=
name|theHelper
expr_stmt|;
block|}
specifier|protected
name|ProxyHelper
parameter_list|()
block|{     }
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
name|InvocationHandler
name|handler
parameter_list|)
block|{
name|ClassLoader
name|combinedLoader
init|=
name|getClassLoaderForInterfaces
argument_list|(
name|loader
argument_list|,
name|interfaces
argument_list|)
decl_stmt|;
return|return
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|combinedLoader
argument_list|,
name|interfaces
argument_list|,
name|handler
argument_list|)
return|;
block|}
comment|/**      * Return a classloader that can see all the given interfaces If the given loader can see all interfaces      * then it is used. If not then a combined classloader of all interface classloaders is returned.      *       * @param loader use supplied class loader      * @param interfaces      * @return classloader that sees all interfaces      */
specifier|private
name|ClassLoader
name|getClassLoaderForInterfaces
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
parameter_list|)
block|{
if|if
condition|(
name|canSeeAllInterfaces
argument_list|(
name|loader
argument_list|,
name|interfaces
argument_list|)
condition|)
block|{
return|return
name|loader
return|;
block|}
name|ProxyClassLoader
name|combined
init|=
operator|new
name|ProxyClassLoader
argument_list|(
name|loader
argument_list|,
name|interfaces
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|currentInterface
range|:
name|interfaces
control|)
block|{
name|combined
operator|.
name|addLoader
argument_list|(
name|currentInterface
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|combined
return|;
block|}
specifier|private
name|boolean
name|canSeeAllInterfaces
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
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|currentInterface
range|:
name|interfaces
control|)
block|{
name|String
name|ifName
init|=
name|currentInterface
operator|.
name|getName
argument_list|()
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|ifClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|ifName
argument_list|,
literal|true
argument_list|,
name|loader
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifClass
operator|!=
name|currentInterface
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|//we need to check all the params/returns as well as the Proxy creation
comment|//will try to create methods for all of this even if they aren't used
comment|//by the client and not available in the clients classloader
for|for
control|(
name|Method
name|m
range|:
name|ifClass
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Class
operator|.
name|forName
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|,
name|loader
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|p
range|:
name|m
operator|.
name|getParameterTypes
argument_list|()
control|)
block|{
name|Class
operator|.
name|forName
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|,
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|Object
name|getProxy
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
name|InvocationHandler
name|handler
parameter_list|)
block|{
return|return
name|HELPER
operator|.
name|getProxyInternal
argument_list|(
name|loader
argument_list|,
name|interfaces
argument_list|,
name|handler
argument_list|)
return|;
block|}
block|}
end_class

end_unit

