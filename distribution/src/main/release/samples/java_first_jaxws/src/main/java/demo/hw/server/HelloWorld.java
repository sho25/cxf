begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_comment
comment|// START SNIPPET: service
end_comment

begin_interface
annotation|@
name|WebService
specifier|public
interface|interface
name|HelloWorld
block|{
name|String
name|sayHi
parameter_list|(
name|String
name|text
parameter_list|)
function_decl|;
comment|/* Advanced usecase of passing an Interface in.  JAX-WS/JAXB does not      * support interfaces directly.  Special XmlAdapter classes need to      * be written to handle them      */
name|String
name|sayHiToUser
parameter_list|(
name|User
name|user
parameter_list|)
function_decl|;
comment|/* Map passing      * JAXB also does not support Maps.  It handles Lists great, but Maps are      * not supported directly.  They also require use of a XmlAdapter to map      * the maps into beans that JAXB can use.       */
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|IntegerUserMapAdapter
operator|.
name|class
argument_list|)
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|getUsers
parameter_list|()
function_decl|;
block|}
end_interface

begin_comment
comment|// END SNIPPET: service
end_comment

end_unit

