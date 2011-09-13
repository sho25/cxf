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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SystemPropertyAction
implements|implements
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
block|{
specifier|final
name|String
name|property
decl_stmt|;
specifier|final
name|String
name|def
decl_stmt|;
specifier|public
name|SystemPropertyAction
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|property
operator|=
name|name
expr_stmt|;
name|def
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|SystemPropertyAction
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|d
parameter_list|)
block|{
name|property
operator|=
name|name
expr_stmt|;
name|def
operator|=
name|d
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see java.security.PrivilegedAction#run()      */
specifier|public
name|String
name|run
parameter_list|()
block|{
if|if
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|property
argument_list|,
name|def
argument_list|)
return|;
block|}
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|SystemPropertyAction
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|def
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|SystemPropertyAction
argument_list|(
name|name
argument_list|,
name|def
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

