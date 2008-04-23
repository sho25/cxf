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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringStyle
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JAXBToStringBuilder
block|{
specifier|private
name|JAXBToStringBuilder
parameter_list|()
block|{             }
specifier|public
specifier|static
name|String
name|valueOf
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|valueOf
argument_list|(
name|object
argument_list|,
name|JAXBToStringStyle
operator|.
name|DEFAULT_STYLE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|valueOf
parameter_list|(
name|Object
name|object
parameter_list|,
name|ToStringStyle
name|style
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|object
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Collection
condition|)
block|{
name|object
operator|=
operator|(
operator|(
name|Collection
operator|)
name|object
operator|)
operator|.
name|toArray
argument_list|()
expr_stmt|;
block|}
return|return
name|ToStringBuilder
operator|.
name|reflectionToString
argument_list|(
name|object
argument_list|,
name|style
argument_list|)
return|;
block|}
block|}
end_class

end_unit

