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
name|microprofile
operator|.
name|client
operator|.
name|mock
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_interface
specifier|public
interface|interface
name|HeadersOnMethodClient
block|{
specifier|default
name|String
name|computeHeader
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
return|return
literal|"HeadersOnMethodClientValueFor"
operator|+
name|headerName
return|;
block|}
comment|//TODO: uncomment once @ClientHeaderParams (plural) is updated to include target of TYPE and METHOD
comment|//    @ClientHeaderParam(name = "MethodHeader1", value = "valueA")
comment|//    @ClientHeaderParam(name = "MethodHeader2", value = {"valueB", "valueC"})
comment|//    @ClientHeaderParam(name = "MethodHeader3", value = "{computeHeader}")
comment|//    @ClientHeaderParam(name = "MethodHeader4",
comment|//        value = "{org.apache.cxf.microprofile.client.mock.HeaderGenerator.generateHeader}")
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
name|String
name|delete
parameter_list|(
name|String
name|someValue
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

