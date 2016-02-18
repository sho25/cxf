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
name|rs
operator|.
name|security
operator|.
name|oidc
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|helpers
operator|.
name|CastUtils
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObject
import|;
end_import

begin_class
specifier|public
class|class
name|ClaimPreference
extends|extends
name|JsonMapObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ESSENTIAL_PROPERTY
init|=
literal|"essential"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_PROPERTY
init|=
literal|"value"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|VALUES_PROPERTY
init|=
literal|"values"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|9105405849730632953L
decl_stmt|;
specifier|public
name|void
name|setEssential
parameter_list|(
name|Boolean
name|essential
parameter_list|)
block|{
name|setProperty
argument_list|(
name|ESSENTIAL_PROPERTY
argument_list|,
name|essential
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getEssential
parameter_list|()
block|{
return|return
name|getBooleanProperty
argument_list|(
name|ESSENTIAL_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setProperty
argument_list|(
name|VALUE_PROPERTY
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|VALUE_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|setProperty
argument_list|(
name|VALUES_PROPERTY
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getValues
parameter_list|()
block|{
name|Object
name|prop
init|=
name|getProperty
argument_list|(
name|VALUES_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|instanceof
name|List
condition|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|prop
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

