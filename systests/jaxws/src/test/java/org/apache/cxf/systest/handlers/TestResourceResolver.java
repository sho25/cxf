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
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|resource
operator|.
name|ResourceResolver
import|;
end_import

begin_class
specifier|public
class|class
name|TestResourceResolver
implements|implements
name|ResourceResolver
block|{
specifier|public
specifier|final
name|InputStream
name|getAsStream
parameter_list|(
specifier|final
name|String
name|string
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
specifier|final
name|String
name|entryName
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clz
parameter_list|)
block|{
if|if
condition|(
name|String
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clz
argument_list|)
operator|&&
literal|"handlerResource"
operator|.
name|equalsIgnoreCase
argument_list|(
name|entryName
argument_list|)
condition|)
block|{
return|return
name|clz
operator|.
name|cast
argument_list|(
operator|new
name|String
argument_list|(
literal|"injectedValue"
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

