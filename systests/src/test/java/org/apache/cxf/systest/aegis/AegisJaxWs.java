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
name|aegis
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
name|WebMethod
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
name|systest
operator|.
name|aegis
operator|.
name|bean
operator|.
name|Item
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_interface
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"AegisJaxWs"
argument_list|)
specifier|public
interface|interface
name|AegisJaxWs
block|{
annotation|@
name|WebMethod
name|void
name|addItem
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"item"
argument_list|)
name|Item
name|item
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"getItemsAsMap"
argument_list|)
name|Map
name|getItemsMap
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"getItemsAsMapSpecified"
argument_list|)
name|Map
argument_list|<
name|Integer
argument_list|,
name|Item
argument_list|>
name|getItemsMapSpecified
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
name|Item
name|getItemByKey
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"key1"
argument_list|)
name|String
name|key1
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"key2"
argument_list|)
name|String
name|key2
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

