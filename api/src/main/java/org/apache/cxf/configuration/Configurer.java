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
package|;
end_package

begin_comment
comment|/**  * The configurer's interface  *   * A class that implements this interface will perform a   * bean's configuration work  */
end_comment

begin_interface
specifier|public
interface|interface
name|Configurer
block|{
name|String
name|DEFAULT_USER_CFG_FILE
init|=
literal|"cxf.xml"
decl_stmt|;
name|String
name|USER_CFG_FILE_PROPERTY_NAME
init|=
literal|"cxf.config.file"
decl_stmt|;
name|String
name|USER_CFG_FILE_PROPERTY_URL
init|=
literal|"cxf.config.file.url"
decl_stmt|;
comment|/**      * set up the Bean's value by using Dependency Injection from the application context      * @param beanInstance the instance of the bean which needs to be configured      */
name|void
name|configureBean
parameter_list|(
name|Object
name|beanInstance
parameter_list|)
function_decl|;
comment|/**      * set up the Bean's value by using Dependency Injection from the application context      * with a proper name. You can use * as the prefix of wildcard name.      * @param name the name of the bean which needs to be configured      * @param beanInstance the instance of bean which need to be configured      */
name|void
name|configureBean
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|beanInstance
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

