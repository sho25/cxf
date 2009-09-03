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
name|maven_plugin
package|;
end_package

begin_class
specifier|public
class|class
name|WsdlOption
extends|extends
name|Option
block|{
comment|/**      * The WSDL file to process.      */
name|String
name|wsdl
decl_stmt|;
comment|/**      * Alternatively to the wsdl string an artifact can be specified      */
name|WsdlArtifact
name|wsdlArtifact
decl_stmt|;
specifier|public
name|String
name|getWsdl
parameter_list|()
block|{
return|return
name|wsdl
return|;
block|}
specifier|public
name|void
name|setWsdl
parameter_list|(
name|String
name|w
parameter_list|)
block|{
name|wsdl
operator|=
name|w
expr_stmt|;
block|}
specifier|public
name|WsdlArtifact
name|getWsdlArtifact
parameter_list|()
block|{
return|return
name|wsdlArtifact
return|;
block|}
specifier|public
name|void
name|setWsdlArtifact
parameter_list|(
name|WsdlArtifact
name|wsdlArtifact
parameter_list|)
block|{
name|this
operator|.
name|wsdlArtifact
operator|=
name|wsdlArtifact
expr_stmt|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|wsdl
operator|!=
literal|null
condition|)
block|{
return|return
name|wsdl
operator|.
name|hashCode
argument_list|()
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|WsdlOption
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|WsdlOption
name|t
init|=
operator|(
name|WsdlOption
operator|)
name|obj
decl_stmt|;
return|return
name|t
operator|.
name|getWsdl
argument_list|()
operator|.
name|equals
argument_list|(
name|getWsdl
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"WSDL: "
argument_list|)
operator|.
name|append
argument_list|(
name|wsdl
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"OutputDir: "
argument_list|)
operator|.
name|append
argument_list|(
name|outputDir
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Extraargs: "
argument_list|)
operator|.
name|append
argument_list|(
name|extraargs
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Packagenames: "
argument_list|)
operator|.
name|append
argument_list|(
name|packagenames
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

