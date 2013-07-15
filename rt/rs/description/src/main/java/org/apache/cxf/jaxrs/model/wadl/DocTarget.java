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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
package|;
end_package

begin_comment
comment|/**  * {@link Description} can use one of DocTarget constants to bind  * itself to a specific WADL element.  * {@link Description} annotations documenting WADL 'resource', 'method',  * 'param' and input 'representation' elements do not have use these constants.     */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DocTarget
block|{
comment|/**      * WADL resource element, in most cases it corresponds       * to the root resource or sub-resource classes      */
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE
init|=
literal|"resource"
decl_stmt|;
comment|/**      * WADL method element, corresponds to a class resource method      */
specifier|public
specifier|static
specifier|final
name|String
name|METHOD
init|=
literal|"method"
decl_stmt|;
comment|/**      * WADL request param or representation elements, correspond to       * input parameters of the resource method      */
specifier|public
specifier|static
specifier|final
name|String
name|PARAM
init|=
literal|"param"
decl_stmt|;
comment|/**      * WADL response representation element, corresponds to       * the return type of the resource method      */
specifier|public
specifier|static
specifier|final
name|String
name|RETURN
init|=
literal|"return"
decl_stmt|;
comment|/**      * WADL request element      */
specifier|public
specifier|static
specifier|final
name|String
name|REQUEST
init|=
literal|"request"
decl_stmt|;
comment|/**      * WADL request element      */
specifier|public
specifier|static
specifier|final
name|String
name|RESPONSE
init|=
literal|"response"
decl_stmt|;
specifier|private
name|DocTarget
parameter_list|()
block|{              }
block|}
end_class

end_unit

