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
name|jaxws
operator|.
name|service
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_interface
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://jaxws.cxf.apache.org/"
argument_list|)
specifier|public
interface|interface
name|GenericsService2
parameter_list|<
name|T
parameter_list|,
name|P
parameter_list|>
block|{
name|P
name|convert
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
name|Value
argument_list|<
name|P
argument_list|>
name|convert2
parameter_list|(
name|Value
argument_list|<
name|T
argument_list|>
name|in
parameter_list|)
function_decl|;
specifier|public
specifier|static
class|class
name|Value
parameter_list|<
name|V
parameter_list|>
block|{
name|V
name|val
decl_stmt|;
specifier|public
name|Value
parameter_list|()
block|{                      }
specifier|public
name|Value
parameter_list|(
name|V
name|v
parameter_list|)
block|{
name|val
operator|=
name|v
expr_stmt|;
block|}
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|val
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|V
name|v
parameter_list|)
block|{
name|val
operator|=
name|v
expr_stmt|;
block|}
block|}
block|}
end_interface

end_unit

