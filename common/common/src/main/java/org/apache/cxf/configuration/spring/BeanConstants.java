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
name|configuration
operator|.
name|spring
package|;
end_package

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|BeanConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_URI
init|=
literal|"http://cxf.apache.org/schemas/configuration/cxf-beans"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_ATTR
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ABSTRACT_ATTR
init|=
literal|"abstract"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CREATED_FROM_API_ATTR
init|=
literal|"createdFromAPI"
decl_stmt|;
comment|/**      * Prevents instantiation.      */
specifier|private
name|BeanConstants
parameter_list|()
block|{             }
block|}
end_class

end_unit

