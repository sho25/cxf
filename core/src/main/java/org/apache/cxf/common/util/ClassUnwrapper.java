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

begin_interface
specifier|public
interface|interface
name|ClassUnwrapper
block|{
comment|/**      * Return a real class for the instance, possibly a proxy.      * @param o instance to get real class for      * @return real class for the instance      */
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
comment|/**      * Return a real class for the class, possibly a proxy class.      * @param clazz class to get real class for      * @return real class for the class      */
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
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Return a real class for the instance, possibly a proxy.      * @param o instance to get real class for      * @return real class for the instance      */
specifier|default
name|Object
name|getRealObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
operator|(
name|o
operator|instanceof
name|Proxy
operator|)
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
block|}
end_interface

end_unit

