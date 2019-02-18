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
name|systests
operator|.
name|java2ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
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
name|cxf
operator|.
name|annotations
operator|.
name|WSDLDocumentation
import|;
end_import

begin_interface
annotation|@
name|WebService
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"A simple service with only one method"
argument_list|)
specifier|public
interface|interface
name|HelloWorldArgs
block|{
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"Simply return the given text"
argument_list|)
name|String
name|sayHi
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"text"
argument_list|)
name|String
name|text
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"date"
argument_list|)
name|Date
name|date
parameter_list|,
name|StringWrapper
index|[]
index|[]
name|wrapper
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
