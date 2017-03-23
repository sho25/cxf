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
name|Proxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ClassHelper
block|{
specifier|static
specifier|final
name|ClassHelper
name|HELPER
decl_stmt|;
static|static
block|{
name|HELPER
operator|=
name|getClassHelper
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ClassHelper
parameter_list|()
block|{     }
specifier|private
specifier|static
name|ClassHelper
name|getClassHelper
parameter_list|()
block|{
name|boolean
name|useSpring
init|=
literal|true
decl_stmt|;
name|String
name|s
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"org.apache.cxf.useSpringClassHelpers"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|useSpring
operator|=
literal|"1"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useSpring
condition|)
block|{
try|try
block|{
return|return
operator|new
name|SpringAopClassHelper
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
operator|new
name|ClassHelper
argument_list|()
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClassInternal
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|getRealObjectInternal
argument_list|(
name|o
argument_list|)
operator|.
name|getClass
argument_list|()
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClassFromClassInternal
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|cls
return|;
block|}
specifier|protected
name|Object
name|getRealObjectInternal
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Proxy
condition|?
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|o
argument_list|)
else|:
name|o
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|getRealClass
argument_list|(
literal|null
argument_list|,
name|o
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClassFromClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|HELPER
operator|.
name|getRealClassFromClassInternal
argument_list|(
name|cls
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Object
name|getRealObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|HELPER
operator|.
name|getRealObjectInternal
argument_list|(
name|o
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClass
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|bus
operator|=
name|bus
operator|==
literal|null
condition|?
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
else|:
name|bus
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
operator|&&
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ClassUnwrapper
name|unwrapper
init|=
operator|(
name|ClassUnwrapper
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|unwrapper
operator|.
name|getRealClass
argument_list|(
name|o
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|HELPER
operator|.
name|getRealClassInternal
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

