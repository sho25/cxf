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
name|type_substitution
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|type_substitution
operator|.
name|Car
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|type_substitution
operator|.
name|CarDealer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|type_substitution
operator|.
name|Porsche
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"CarDealer"
argument_list|)
specifier|public
class|class
name|CarDealerImpl
implements|implements
name|CarDealer
block|{
specifier|public
name|List
argument_list|<
name|Car
argument_list|>
name|getSedans
parameter_list|(
name|String
name|carType
parameter_list|)
block|{
if|if
condition|(
literal|"Porsche"
operator|.
name|equalsIgnoreCase
argument_list|(
name|carType
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Car
argument_list|>
name|cars
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|cars
operator|.
name|add
argument_list|(
name|newPorsche
argument_list|(
literal|"Boxster"
argument_list|,
literal|"1998"
argument_list|,
literal|"white"
argument_list|)
argument_list|)
expr_stmt|;
name|cars
operator|.
name|add
argument_list|(
name|newPorsche
argument_list|(
literal|"BoxsterS"
argument_list|,
literal|"1999"
argument_list|,
literal|"red"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cars
return|;
block|}
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Not a dealer of: "
operator|+
name|carType
argument_list|)
throw|;
block|}
annotation|@
name|SOAPBinding
argument_list|(
name|parameterStyle
operator|=
name|SOAPBinding
operator|.
name|ParameterStyle
operator|.
name|BARE
argument_list|)
specifier|public
name|Car
name|tradeIn
parameter_list|(
name|Car
name|oldCar
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|oldCar
operator|instanceof
name|Porsche
operator|)
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Expected Porsche, received, "
operator|+
name|oldCar
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|Porsche
name|porsche
init|=
operator|(
name|Porsche
operator|)
name|oldCar
decl_stmt|;
if|if
condition|(
literal|"Porsche"
operator|.
name|equals
argument_list|(
name|porsche
operator|.
name|getMake
argument_list|()
argument_list|)
operator|&&
literal|"GT2000"
operator|.
name|equals
argument_list|(
name|porsche
operator|.
name|getModel
argument_list|()
argument_list|)
operator|&&
literal|"2000"
operator|.
name|equals
argument_list|(
name|porsche
operator|.
name|getYear
argument_list|()
argument_list|)
operator|&&
literal|"white"
operator|.
name|equals
argument_list|(
name|porsche
operator|.
name|getColor
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|newPorsche
argument_list|(
literal|"911GT3"
argument_list|,
literal|"2007"
argument_list|,
literal|"black"
argument_list|)
return|;
block|}
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Invalid Porsche Car"
argument_list|)
throw|;
block|}
specifier|private
name|Porsche
name|newPorsche
parameter_list|(
name|String
name|model
parameter_list|,
name|String
name|year
parameter_list|,
name|String
name|color
parameter_list|)
block|{
name|Porsche
name|porsche
init|=
operator|new
name|Porsche
argument_list|()
decl_stmt|;
name|porsche
operator|.
name|setMake
argument_list|(
literal|"Porsche"
argument_list|)
expr_stmt|;
name|porsche
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|porsche
operator|.
name|setYear
argument_list|(
name|year
argument_list|)
expr_stmt|;
name|porsche
operator|.
name|setColor
argument_list|(
name|color
argument_list|)
expr_stmt|;
return|return
name|porsche
return|;
block|}
block|}
end_class

end_unit

